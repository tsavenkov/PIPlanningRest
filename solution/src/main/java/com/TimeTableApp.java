/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com;

import com.domain.DomainFeature;
import com.domain.PiPlanning;
import com.domain.DomainSprint;
import com.domain.DomainUserStory;
import com.solver.TimeTableConstraintProvider;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TimeTableApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTableApp.class);
    private static final List<DomainSprint> sprintList = new ArrayList<>(2);
    private static final List<DomainUserStory> userStoriesList = new ArrayList<>();
    private static final List<DomainFeature> featuresList = new ArrayList<>();

    public static PiPlanning solve(PiPlanning problem) {
        SolverFactory<PiPlanning> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(PiPlanning.class)
                .withEntityClasses(DomainUserStory.class)
                .withConstraintProviderClass(TimeTableConstraintProvider.class)
                // The solver runs only for 5 seconds on this small dataset.
                // It's recommended to run for at least 5 minutes ("5m") otherwise.
                .withTerminationSpentLimit(Duration.ofSeconds(20)));

//        // Load the problem
//        PiPlanning problem = generateDemoData();

        // Solve the problem
        Solver<PiPlanning> solver = solverFactory.buildSolver();
        PiPlanning solution = solver.solve(problem);

        //calculate unused capacity for each sprint
        for (DomainSprint sprint : solution.getSprintList()) {
            int usedFECapacity = solution.getUserStoryList().stream().filter(a -> a.getSprint().getName().equals(sprint.getName())).mapToInt(DomainUserStory::getFeCapacity).sum();
            int usedBECapacity = solution.getUserStoryList().stream().filter(a -> a.getSprint().getName().equals(sprint.getName())).mapToInt(DomainUserStory::getBeCapacity).sum();
            int usedSDCapacity = solution.getUserStoryList().stream().filter(a -> a.getSprint().getName().equals(sprint.getName())).mapToInt(DomainUserStory::getSdCapacity).sum();

            sprint.setUnusedFECapacity(sprint.getMaxFeCapacity() - usedFECapacity);
            sprint.setUnusedBECapacity(sprint.getMaxBeCapacity() -  usedBECapacity);
            sprint.setUnusedSDCapacity(sprint.getMaxSdCapacity() - usedSDCapacity);
        }
        // Visualize the solution
        printTimetable(solution);
        return solution;
    }

    private static void generateSprints() {
        sprintList.add(DomainSprint.builder()
                .id(1)
                .name("Sprint_1")
                .maxSdCapacity(12)
                .maxBeCapacity(17)
                .maxFeCapacity(10).build());
        sprintList.add(DomainSprint.builder()
                .id(2)
                .name("Sprint_2")
                .maxSdCapacity(12)
                .maxBeCapacity(11)
                .maxFeCapacity(10).build());
        sprintList.add(DomainSprint.builder()
                .id(3)
                .name("Sprint_3")
                .maxSdCapacity(12)
                .maxBeCapacity(11)
                .maxFeCapacity(10).build());
        sprintList.add(DomainSprint.builder()
                .id(4)
                .name("Sprint_4")
                .maxSdCapacity(7)
                .maxBeCapacity(11)
                .maxFeCapacity(10).build());

        sprintList.add(DomainSprint.builder()
                .id(5)
                .name("Sprint_5")
                .maxSdCapacity(7)
                .maxBeCapacity(11)
                .maxFeCapacity(10).build());
    }

    private static void generateFeatures() {
        featuresList.add(DomainFeature.builder()
                .id(1)
                .subject("Plain Vanilla")
                .priority(3)
                .build());
        featuresList.add(DomainFeature.builder()
                .id(2)
                .priority(1)
                .subject("Table View New/Change")
                .build());
    }

    private static void generateUserStories() {
        userStoriesList.add(DomainUserStory.builder()
                .id(1)
                .subject("US1")
                .sprint(sprintList.get(0))
                .feCapacity(3)
                .beCapacity(4)
                .sdCapacity(5)
                .feature(featuresList.get(0))
                .build());
        userStoriesList.add(DomainUserStory.builder()
                .id(2)
                .sprint(sprintList.get(0))
                .subject("US2")
                .feCapacity(4)
                .beCapacity(4)
                .sdCapacity(5)
                .feature(featuresList.get(0))
                .build());
        userStoriesList.add(DomainUserStory.builder()
                .id(3)
                .subject("US3")
                .sprint(sprintList.get(0))
                .feCapacity(4)
                .beCapacity(4)
                .sdCapacity(5)
                .feature(featuresList.get(1))
                .build());
        userStoriesList.add(DomainUserStory.builder()
                .id(4)
                .sprint(sprintList.get(0))
                .subject("US4")
                .feCapacity(4)
                .beCapacity(4)
                .sdCapacity(5)
                .feature(featuresList.get(1))
                .build());
    }

    public static PiPlanning generateDemoData() {
        generateSprints();
        generateFeatures();
        generateUserStories();

        return new PiPlanning(null, sprintList, null, userStoriesList);
    }

    private static void printTimetable(PiPlanning piPlanning) {
        LOGGER.info("Printing out the results");
        List<DomainSprint> sprintList = piPlanning.getSprintList();
        List<DomainUserStory> userStoryList = piPlanning.getUserStoryList();

        for (DomainUserStory us : piPlanning.getUserStoryList()) {
            LOGGER.info("Feature: " + us.getFeature().getSubject() + "  " + us.getSubject() + " in " + us.getSprint().getName());
        }


        for (DomainSprint sprint : piPlanning.getSprintList()) {
            LOGGER.info(sprint.getName());
            LOGGER.info("");
            piPlanning.getUserStoryList().stream().filter(a -> a.getSprint().getName().equals(sprint.getName())).forEach(us -> LOGGER.info(us.getSubject()));
//            int usedFECapacity = piPlanning.getUserStoryList().stream().filter(a -> a.getSprint().getName().equals(sprint.getName())).mapToInt(DomainUserStory::getFeCapacity).sum();
//            int usedBECapacity = piPlanning.getUserStoryList().stream().filter(a -> a.getSprint().getName().equals(sprint.getName())).mapToInt(DomainUserStory::getBeCapacity).sum();
//            int usedSDCapacity = piPlanning.getUserStoryList().stream().filter(a -> a.getSprint().getName().equals(sprint.getName())).mapToInt(DomainUserStory::getSdCapacity).sum();
            LOGGER.info(sprint.getName() + " total FE unused = " + sprint.getUnusedFECapacity());
            LOGGER.info(sprint.getName() + " total BE unused = " + sprint.getUnusedBECapacity());
            LOGGER.info(sprint.getName() + " total SD unused = " + sprint.getUnusedSDCapacity());
            LOGGER.info("--------------------------------------------------");
        }
    }
}
