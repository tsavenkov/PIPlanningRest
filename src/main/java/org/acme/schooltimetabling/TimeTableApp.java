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

package org.acme.schooltimetabling;

import org.acme.schooltimetabling.domain.Feature;
import org.acme.schooltimetabling.domain.Sprint;
import org.acme.schooltimetabling.domain.PiPlanning;
import org.acme.schooltimetabling.domain.UserStory;
import org.acme.schooltimetabling.solver.TimeTableConstraintProvider;
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
                .withTerminationSpentLimit(Duration.ofSeconds(5)));

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
                .name("Sprint_1")
                .maxSdCapacity(5)
                .maxBeCapacity(17)
                .maxFeCapacity(11).build());
        sprintList.add(Sprint.builder()
                .name("Sprint_2")
                .maxSdCapacity(7)
                .maxBeCapacity(11)
                .maxFeCapacity(8).build());
        sprintList.add(Sprint.builder()
                .name("Sprint_3")
                .maxSdCapacity(7)
                .maxBeCapacity(18)
                .maxFeCapacity(11).build());


        List<UserStory> userStoriesList = new ArrayList<>();
        long id = 0;

        Feature feature1 = Feature.builder()
                .subject("Plain Vanilla")
                .build();
        Feature feature2 = Feature.builder()
                .subject("Table View New/Change")
                .build();


        userStoriesList.add(UserStory.builder()
                .id(id++)
                .subject("FE: Renaming field")
                .sprint(sprintList.get(0))
                .feCapacity(2)
                .feature(feature1)
                .build());
        userStoriesList.add(UserStory.builder()
                .id(id++)
                .sprint(sprintList.get(0))
                .subject("BE: Renaming and adding the new user")
                .beCapacity(8)
                .feature(feature1)
                .build());
        userStoriesList.add(UserStory.builder()
                .id(id++)
                .sprint(sprintList.get(0))
                .subject("SD: Renaming field")
                .sdCapacity(2)
                .feature(feature1)
                .build());


        userStoriesList.add(UserStory.builder()
                .id(id++)
                .subject("FE: T1 - Reuse configuration form")
                .sprint(sprintList.get(0))
                .feCapacity(8)
                .feature(feature2)
                .build());
        userStoriesList.add(UserStory.builder()
                .id(id++)
                .sprint(sprintList.get(0))
                .subject("FE: T2 - Adapt form rules")
                .feCapacity(13)
                .feature(feature2)
                .build());
        userStoriesList.add(UserStory.builder()
                .id(id++)
                .sprint(sprintList.get(0))
                .subject("FE: T3 - Handle transition for Cancel action")
                .feCapacity(3)
                .feature(feature2)
                .build());
        userStoriesList.add(UserStory.builder()
                .id(id++)
                .sprint(sprintList.get(0))

                .subject("FE: T4 - Handle Save action")
                .feCapacity(5)
                .feature(feature2)
                .build());
        userStoriesList.add(UserStory.builder()
                .id(id++)
                .sprint(sprintList.get(0))
                .subject("BE: T4 - Async call for saving options")
                .beCapacity(13)
                .feature(feature2)
                .build());

        return new PiPlanning(null, sprintList, userStoriesList);
    }

    private static void printTimetable(PiPlanning piPlanning) {
        LOGGER.info("Printing out the results");
        List<Sprint> sprintList = piPlanning.getSprintList();
        List<UserStory> userStoryList = piPlanning.getUserStoryList();

        for (UserStory us : userStoryList) {
            LOGGER.info(us.getSubject() + " in " + userStoryList.get(0).getSprint().getName());

        }
    }
}
