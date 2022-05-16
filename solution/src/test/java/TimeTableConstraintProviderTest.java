/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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


import com.domain.DomainFeature;
import com.domain.DomainSprint;
import com.domain.DomainUserStory;
import com.domain.PiPlanning;
import com.solver.TimeTableConstraintProvider;
import org.junit.jupiter.api.Test;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;

class TimeTableConstraintProviderTest {

    private static final DomainSprint sprint1 = DomainSprint.builder()
            .id(1)
            .name("Sprint_1")
            .maxFeCapacity(10).build();
    private static final DomainSprint sprint2 = DomainSprint.builder()
            .id(2)
            .name("Sprint_2")
            .maxFeCapacity(10).build();
    private static final DomainSprint sprint6 = DomainSprint.builder()
            .id(6)
            .name("Sprint_6")
            .maxFeCapacity(1000).build();

    ConstraintVerifier<TimeTableConstraintProvider, PiPlanning> constraintVerifier = ConstraintVerifier.build(
            new TimeTableConstraintProvider(), PiPlanning.class, DomainUserStory.class);


    @Test
    void sdStoryLaterFeTestWrong() {
        DomainFeature feature1 = DomainFeature.builder()
                .id(1)
                .subject("Plain Vanilla")
                .priority(1)
                .build();

        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .feCapacity(5)
                .feature(feature1)
                .build();
        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint2)
                .sdCapacity(1)
                .feature(feature1)
                .build();

        constraintVerifier.verifyThat(TimeTableConstraintProvider::sdCapacityGoesFirst)
                .given(us1, us2)
                .penalizesBy(1);
    }

    @Test
    void sdStoryLaterbeTestWrong() {
        DomainFeature feature1 = DomainFeature.builder()
                .id(1)
                .subject("Plain Vanilla")
                .priority(1)
                .build();

        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .beCapacity(5)
                .feature(feature1)
                .build();
        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint2)
                .sdCapacity(1)
                .feature(feature1)
                .build();

        constraintVerifier.verifyThat(TimeTableConstraintProvider::sdCapacityGoesFirst)
                .given(us1, us2)
                .penalizesBy(1);
    }

    @Test
    void sdStorySameSprintFeOK() {
        DomainFeature feature1 = DomainFeature.builder()
                .id(1)
                .subject("Plain Vanilla")
                .priority(1)
                .build();

        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .feCapacity(5)
                .feature(feature1)
                .build();
        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint1)
                .sdCapacity(1)
                .feature(feature1)
                .build();

        constraintVerifier.verifyThat(TimeTableConstraintProvider::sdCapacityGoesFirst)
                .given(us1, us2)
                .penalizesBy(0);
    }

    @Test
    void sdStorySameSprintBeOK() {
        DomainFeature feature1 = DomainFeature.builder()
                .id(1)
                .subject("Plain Vanilla")
                .priority(1)
                .build();

        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .beCapacity(5)
                .feature(feature1)
                .build();
        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint1)
                .sdCapacity(1)
                .feature(feature1)
                .build();

        constraintVerifier.verifyThat(TimeTableConstraintProvider::sdCapacityGoesFirst)
                .given(us1, us2)
                .penalizesBy(0);
    }

    @Test
    void sdStoryLaterSprintBeWrong2() {
        DomainFeature feature1 = DomainFeature.builder()
                .id(1)
                .subject("Plain Vanilla")
                .priority(1)
                .build();

        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .beCapacity(5)
                .feature(feature1)
                .build();
        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint2)
                .sdCapacity(1)
                .feature(feature1)
                .build();
        DomainUserStory us3 = DomainUserStory.builder()
                .id(3)
                .subject("us3")
                .sprint(sprint1)
                .feCapacity(1)
                .feature(feature1)
                .build();

        constraintVerifier.verifyThat(TimeTableConstraintProvider::sdCapacityGoesFirst)
                .given(us1, us2, us3)
                .penalizesBy(2);
    }

    @Test
    void sdStorySameLaterSprintBeWrong1() {
        DomainFeature feature1 = DomainFeature.builder()
                .id(1)
                .subject("Plain Vanilla")
                .priority(1)
                .build();

        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .beCapacity(5)
                .feature(feature1)
                .build();
        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint2)
                .sdCapacity(1)
                .feature(feature1)
                .build();
        DomainUserStory us3 = DomainUserStory.builder()
                .id(3)
                .subject("us3")
                .sprint(sprint2)
                .feCapacity(1)
                .feature(feature1)
                .build();

        constraintVerifier.verifyThat(TimeTableConstraintProvider::sdCapacityGoesFirst)
                .given(us1, us2, us3)
                .penalizesBy(1);
    }

}
