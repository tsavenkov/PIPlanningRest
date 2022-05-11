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

package org.acme.piplanning.solver;


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
    void sprintFEConflict() {

        DomainFeature feature1 = DomainFeature.builder()
                .subject("Plain Vanilla")
                .build();
        DomainFeature feature2 = DomainFeature.builder()
                .subject("Table View New/Change")
                .build();

        DomainUserStory firstUserStory = DomainUserStory.builder()
                .id(1)
                .subject("FE: T1 - Reuse configuration form")
                .sprint(sprint1)
                .feCapacity(5)
                .feature(feature1)
                .build();

        DomainUserStory conflictingUserStory = DomainUserStory.builder()
                .id(2)
                .subject("FE: T2- Reuse configuration form")
                .sprint(sprint1)
                .feCapacity(10)
                .feature(feature1)
                .build();

        DomainUserStory nonConflictingUserSTory = DomainUserStory.builder()
                .id(3)
                .subject("FE: T3 - Reuse configuration form")
                .sprint(sprint2)
                .feCapacity(3)
                .feature(feature2)
                .build();

        DomainUserStory US4 = DomainUserStory.builder()
                .id(4)
                .subject("FE: T4 - Reuse configuration form")
                .sprint(sprint2)
                .feCapacity(8)
                .feature(feature2)
                .build();

        constraintVerifier.verifyThat(TimeTableConstraintProvider::feStoryPointsConflictTotal)
                .given(firstUserStory, conflictingUserStory, nonConflictingUserSTory, US4)
                .penalizesBy(6);
    }

    @Test
    void PriorityConflict() {
        DomainFeature feature1 = DomainFeature.builder()
                .subject("DomainFeature with lowest priority")
                .priority(1)
                .build();
        DomainFeature feature2 = DomainFeature.builder()
                .subject("DomainFeature with the highest priority")
                .priority(2)
                .build();


        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .feCapacity(3)
                .feature(feature1)
                .build();

        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint1)
                .feCapacity(4)
                .feature(feature1)
                .build();

        DomainUserStory us3 = DomainUserStory.builder()
                .id(3)
                .subject("us3")
                .sprint(sprint2)
                .feCapacity(4)
                .feature(feature2)
                .build();

        DomainUserStory us4 = DomainUserStory.builder()
                .id(4)
                .subject("us4")
                .sprint(sprint2)
                .feCapacity(4)
                .feature(feature2)
                .build();

        constraintVerifier.verifyThat(TimeTableConstraintProvider::featurePriority)
                .given(us1, us2, us3, us4)
                .penalizesBy(4);
    }

    @Test
    void PriorityConflict2() {
        DomainFeature feature1 = DomainFeature.builder()
                .id(1)
                .subject("Plain Vanilla")
                .priority(3)
                .build();
        DomainFeature feature2 = DomainFeature.builder()
                .id(2)
                .subject("Table View New/Change")
                .priority(1)
                .build();

        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint2)
                .feature(feature1)
                .build();
        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint1)
                .feature(feature2)
                .build();

        constraintVerifier.verifyThat(TimeTableConstraintProvider::featurePriority)
                .given(us1, us2)
                .penalizesBy(1);
    }

    @Test
    void outOfScopeConflict() {
        DomainFeature feature1 = DomainFeature.builder()
                .id(1)
                .subject("Plain Vanilla")
                .priority(1)
                .build();

        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .feature(feature1)
                .build();
        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint6)
                .feature(feature1)
                .build();
        DomainUserStory us3 = DomainUserStory.builder()
                .id(3)
                .subject("us3")
                .sprint(sprint6)
                .feature(feature1)
                .build();

        constraintVerifier.verifyThat(TimeTableConstraintProvider::sprintCapacityUsage)
                .given(us1, us2, us3)
                .penalizesBy(2);
    }

    @Test
    void noOutOfScopeConflict() {
        DomainFeature feature1 = DomainFeature.builder()
                .id(1)
                .subject("Plain Vanilla")
                .priority(1)
                .build();

        DomainUserStory us1 = DomainUserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .feature(feature1)
                .build();
        DomainUserStory us2 = DomainUserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint1)
                .feature(feature1)
                .build();
        DomainUserStory us3 = DomainUserStory.builder()
                .id(3)
                .subject("us3")
                .sprint(sprint1)
                .feature(feature1)
                .build();

        constraintVerifier.verifyThat(TimeTableConstraintProvider::sprintCapacityUsage)
                .given(us1, us2, us3)
                .penalizesBy(0);
    }
//
//    @Test
//    void teacherConflict() {
//        String conflictingTeacher = "Teacher1";
//        DomainUserStory firstUserStory = new DomainUserStory(1, "Subject1", conflictingTeacher, "Group1", TIMESLOT1, SPRINT_1);
//        DomainUserStory conflictingUserStory = new DomainUserStory(2, "Subject2", conflictingTeacher, "Group2", TIMESLOT1, SPRINT_2);
//        DomainUserStory nonConflictingLesson = new DomainUserStory(3, "Subject3", "Teacher2", "Group3", TIMESLOT2, SPRINT_1);
//        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherConflict)
//                .given(firstUserStory, conflictingUserStory, nonConflictingLesson)
//                .penalizesBy(1);
//    }
//
//    @Test
//    void studentGroupConflict() {
//        String conflictingGroup = "Group1";
//        DomainUserStory firstUserStory = new DomainUserStory(1, "Subject1", "Teacher1", conflictingGroup, TIMESLOT1, SPRINT_1);
//        DomainUserStory conflictingUserStory = new DomainUserStory(2, "Subject2", "Teacher2", conflictingGroup, TIMESLOT1, SPRINT_2);
//        DomainUserStory nonConflictingLesson = new DomainUserStory(3, "Subject3", "Teacher3", "Group3", TIMESLOT2, SPRINT_1);
//        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupConflict)
//                .given(firstUserStory, conflictingUserStory, nonConflictingLesson)
//                .penalizesBy(1);
//    }
//
//    @Test
//    void teacherRoomStability() {
//        String teacher = "Teacher1";
//        DomainUserStory lessonInFirstRoom = new DomainUserStory(1, "Subject1", teacher, "Group1", TIMESLOT1, SPRINT_1);
//        DomainUserStory lessonInSameRoom = new DomainUserStory(2, "Subject2", teacher, "Group2", TIMESLOT1, SPRINT_1);
//        DomainUserStory lessonInDifferentRoom = new DomainUserStory(3, "Subject3", teacher, "Group3", TIMESLOT1, SPRINT_2);
//        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherRoomStability)
//                .given(lessonInFirstRoom, lessonInDifferentRoom, lessonInSameRoom)
//                .penalizesBy(2);
//    }
//
//    @Test
//    void teacherTimeEfficiency() {
//        String teacher = "Teacher1";
//        DomainUserStory singleLessonOnMonday = new DomainUserStory(1, "Subject1", teacher, "Group1", TIMESLOT1, SPRINT_1);
//        DomainUserStory firstTuesdayUserStory = new DomainUserStory(2, "Subject2", teacher, "Group2", TIMESLOT2, SPRINT_1);
//        DomainUserStory secondTuesdayLesson = new DomainUserStory(3, "Subject3", teacher, "Group3", TIMESLOT3, SPRINT_1);
//        DomainUserStory thirdTuesdayLessonWithGap = new DomainUserStory(4, "Subject4", teacher, "Group4", TIMESLOT4, SPRINT_1);
//        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherTimeEfficiency)
//                .given(singleLessonOnMonday, firstTuesdayUserStory, secondTuesdayLesson, thirdTuesdayLessonWithGap)
//                .rewardsWith(1); // Second tuesday lesson immediately follows the first.
//    }
//
//    @Test
//    void studentGroupSubjectVariety() {
//        String studentGroup = "Group1";
//        String repeatedSubject = "Subject1";
//        DomainUserStory mondayLesson = new DomainUserStory(1, repeatedSubject, "Teacher1", studentGroup, TIMESLOT1, SPRINT_1);
//        DomainUserStory firstTuesdayUserStory = new DomainUserStory(2, repeatedSubject, "Teacher2", studentGroup, TIMESLOT2, SPRINT_1);
//        DomainUserStory secondTuesdayLesson = new DomainUserStory(3, repeatedSubject, "Teacher3", studentGroup, TIMESLOT3, SPRINT_1);
//        DomainUserStory thirdTuesdayLessonWithDifferentSubject = new DomainUserStory(4, "Subject2", "Teacher4", studentGroup, TIMESLOT4, SPRINT_1);
//        DomainUserStory lessonInAnotherGroup = new DomainUserStory(5, repeatedSubject, "Teacher5", "Group2", TIMESLOT1, SPRINT_1);
//        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupSubjectVariety)
//                .given(mondayLesson, firstTuesdayUserStory, secondTuesdayLesson, thirdTuesdayLessonWithDifferentSubject,
//                        lessonInAnotherGroup)
//                .penalizesBy(1); // Second tuesday lesson immediately follows the first.
//    }

}
