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

    public static PiPlanning generateDemoData() {

        List<Sprint> sprintList = new ArrayList<>(3);
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
//        sprintList.add(Sprint.builder()
//                .name("Sprint_3")
//                .maxSdCapacity(7)
//                .maxBeCapacity(18)
//                .maxFeCapacity(10).build());


        List<UserStory> userStoriesList = new ArrayList<>();
        int id = 0;

        Feature feature1 = Feature.builder()
                .subject("Plain Vanilla")
                .priority(1)
                .build();
        Feature feature2 = Feature.builder()
                .priority(2)
                .subject("Table View New/Change")
                .build();


        userStoriesList.add(UserStory.builder()
                .id(id++)
                .subject("FE1")
                .sprint(sprintList.get(0))
                .feCapacity(2)
                .feature(feature1)
                .build());
        userStoriesList.add(UserStory.builder()
                .id(id++)
                .sprint(sprintList.get(0))
                .subject("FE2")
                .feCapacity(2)
                .feature(feature1)
                .build());
//        userStoriesList.add(UserStory.builder()
//                .id(id++)
//                .sprint(sprintList.get(0))
//                .subject("SD: Renaming field")
//                .feCapacity(3)
//                .feature(feature1)
//                .build());

        userStoriesList.add(UserStory.builder()
                .id(id++)
                .subject("FE3")
                .sprint(sprintList.get(0))
                .feCapacity(4)
                .feature(feature2)
                .build());
        userStoriesList.add(UserStory.builder()
                .id(id++)
                .sprint(sprintList.get(0))
                .subject("FE4")
                .feCapacity(4)
                .feature(feature2)
                .build());
//        userStoriesList.add(UserStory.builder()
//                .id(id++)
//                .sprint(sprintList.get(0))
//                .subject("FE: T3 - Handle transition for Cancel action")
//                .feCapacity(6)
//                .feature(feature1)
//                .build());
//        userStoriesList.add(UserStory.builder()
//                .id(id++)
//                .sprint(sprintList.get(1))
//
//                .subject("FE: T4 - Handle Save action")
//                .feCapacity(5)
//                .feature(feature2)
//                .build());
//        userStoriesList.add(UserStory.builder()
//                .id(id++)
//                .sprint(sprintList.get(2))
//                .subject("BE: T4 - Async call for saving options")
//                .beCapacity(13)
//                .feCapacity(4)
//                .feature(feature2)
//                .build());

        return new PiPlanning(null, sprintList, userStoriesList);
    }

    private static void printTimetable(PiPlanning piPlanning) {
        LOGGER.info("Printing out the results");
        List<Sprint> sprintList = piPlanning.getSprintList();
        List<UserStory> userStoryList = piPlanning.getUserStoryList();

        for (UserStory us : piPlanning.getUserStoryList()) {
            LOGGER.info(us.getSubject() + " in " + us.getSprint().getName());
        }

        for (Sprint sprint : piPlanning.getSprintList()) {
            LOGGER.info(sprint.getName() + " total fe capacity " + piPlanning.getUserStoryList().stream().filter(a -> a.getSprint().getName().equals(sprint.getName())).mapToInt(UserStory::getFeCapacity).sum());
        }
    }
}
