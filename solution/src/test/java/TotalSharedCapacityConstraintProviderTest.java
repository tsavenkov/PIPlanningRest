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

class TotalSharedCapacityConstraintProviderTest {

    private static final DomainSprint sprint1 = DomainSprint.builder()
            .id(1)
            .name("Sprint_1")
            .maxFeCapacity(5)
            .maxBeCapacity(5)
            .sharedCapacity(5).build();           //total possible capa 15 + delta 2, so 17 allowed. but 17 wil lbe penalized with
//    private static final DomainSprint sprint2 = DomainSprint.builder()
//            .id(2)
//            .name("Sprint_2")
//            .maxFeCapacity(10)
//            .maxBeCapacity(6)
//            .sharedCapacity(3).build();
//    private static final DomainSprint sprint6 = DomainSprint.builder()
//            .id(6)
//            .name("Sprint_6")
//            .maxFeCapacity(1000).build();

    ConstraintVerifier<TimeTableConstraintProvider, PiPlanning> constraintVerifier = ConstraintVerifier.build(
            new TimeTableConstraintProvider(), PiPlanning.class, DomainUserStory.class);

    @Test
    void sprintFENoConflict() {
        DomainFeature feature1 = DomainFeature.builder()
                .subject("f1")
                .build();
        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .befeTotalCapacity(10)
                .feature(feature1)
                .build();
        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint1)
                .befeTotalCapacity(5)
                .feature(feature1)
                .build();
        constraintVerifier.verifyThat(TimeTableConstraintProvider::febeSharedStoryPointsConflictTotal)
                .given(us1, us2)
                .penalizesBy(0);         //with delta 2
    }

    @Test
    void sprintFE1Conflict() {
        DomainFeature feature1 = DomainFeature.builder()
                .subject("f1")
                .build();
        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .befeTotalCapacity(10)
                .feature(feature1)
                .build();
        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint1)
                .befeTotalCapacity(8)
                .feature(feature1)
                .build();
        constraintVerifier.verifyThat(TimeTableConstraintProvider::febeSharedStoryPointsConflictTotal)
                .given(us1, us2)
                .penalizesBy(1);         //with delta 2 + shared 5 and fe 10 so total we can place 17. Penalty 1
    }

    @Test
    void sprintFEBE0Conflict() {
        DomainFeature feature1 = DomainFeature.builder()
                .subject("f1")
                .build();
        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .befeTotalCapacity(5)
                .feature(feature1)
                .build();
        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint1)
                .befeTotalCapacity(8)
                .feature(feature1)
                .build();
        constraintVerifier.verifyThat(TimeTableConstraintProvider::febeSharedStoryPointsConflictTotal)
                .given(us1, us2)
                .penalizesBy(0);         //with delta 2 + shared 5 and fe 10 so total we can place 17. Penalty 1
    }


}
