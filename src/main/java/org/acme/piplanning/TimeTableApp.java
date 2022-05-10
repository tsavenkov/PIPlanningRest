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

package org.acme.piplanning;

import org.acme.piplanning.domain.Feature;
import org.acme.piplanning.domain.Sprint;
import org.acme.piplanning.domain.PiPlanning;
import org.acme.piplanning.domain.UserStory;
import org.acme.piplanning.solver.TimeTableConstraintProvider;
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
    private static final List<Sprint> sprintList = new ArrayList<>(2);
    private static final List<UserStory> userStoriesList = new ArrayList<>();
    private static final List<Feature> featuresList = new ArrayList<>();

    public static void main(String[] args) {
        SolverFactory<PiPlanning> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(PiPlanning.class)
                .withEntityClasses(UserStory.class)
                .withConstraintProviderClass(TimeTableConstraintProvider.class)
                // The solver runs only for 5 seconds on this small dataset.
                // It's recommended to run for at least 5 minutes ("5m") otherwise.
                .withTerminationSpentLimit(Duration.ofSeconds(10)));

        // Load the problem
        PiPlanning problem = generateDemoData();

        // Solve the problem
        Solver<PiPlanning> solver = solverFactory.buildSolver();
        PiPlanning solution = solver.solve(problem);

        // Visualize the solution
        printTimetable(solution);
    }


    private static void generateSprints() {
        sprintList.add(Sprint.builder()
                .id(1)
                .name("Sprint_1")
                .maxSdCapacity(5)
                .maxBeCapacity(17)
                .maxFeCapacity(10).build());
        sprintList.add(Sprint.builder()
                .id(2)
                .name("Sprint_2")
                .maxSdCapacity(7)
                .maxBeCapacity(11)
                .maxFeCapacity(10).build());
    }

    private static void generateFeatures() {
        featuresList.add(Feature.builder()
                .id(1)
                .subject("Plain Vanilla")
                .priority(3)
                .build());
        featuresList.add(Feature.builder()
                .id(2)
                .priority(1)
                .subject("Table View New/Change")
                .build());
    }

    private static void generateUserStories() {
        userStoriesList.add(UserStory.builder()
                .id(1)
                .subject("US1")
                .sprint(sprintList.get(0))
                .feCapacity(3)
                .feature(featuresList.get(0))
                .build());
        userStoriesList.add(UserStory.builder()
                .id(2)
                .sprint(sprintList.get(0))
                .subject("US2")
                .feCapacity(4)
                .feature(featuresList.get(0))
                .build());
        userStoriesList.add(UserStory.builder()
                .id(3)
                .subject("US3")
                .sprint(sprintList.get(0))
                .feCapacity(4)
                .feature(featuresList.get(1))
                .build());
        userStoriesList.add(UserStory.builder()
                .id(4)
                .sprint(sprintList.get(0))
                .subject("US4")
                .feCapacity(4)
                .feature(featuresList.get(1))
                .build());
    }

    public static PiPlanning generateDemoData() {
        generateSprints();
        generateFeatures();
        generateUserStories();

        return new PiPlanning(null, sprintList, userStoriesList);
    }

    private static void printTimetable(PiPlanning piPlanning) {
        LOGGER.info("Printing out the results");
        List<Sprint> sprintList = piPlanning.getSprintList();
        List<UserStory> userStoryList = piPlanning.getUserStoryList();

        for (UserStory us : piPlanning.getUserStoryList()) {
            LOGGER.info("Feature: " + us.getFeature().getSubject() + "  " + us.getSubject() + " in " + us.getSprint().getName());
        }


        for (Sprint sprint : piPlanning.getSprintList()) {
            LOGGER.info(sprint.getName());
            LOGGER.info("");
            piPlanning.getUserStoryList().stream().filter(a -> a.getSprint().getName().equals(sprint.getName())).forEach(us -> LOGGER.info(us.getSubject()));
            LOGGER.info(sprint.getName() + " total fe capacity " + piPlanning.getUserStoryList().stream().filter(a -> a.getSprint().getName().equals(sprint.getName())).mapToInt(UserStory::getFeCapacity).sum());
            LOGGER.info("----------");
        }
    }
}
