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

import org.acme.piplanning.domain.Feature;
import org.acme.piplanning.domain.PiPlanning;
import org.acme.piplanning.domain.Sprint;
import org.acme.piplanning.domain.UserStory;
import org.junit.jupiter.api.Test;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;

class TimeTableConstraintProviderTest {

    private static final Sprint sprint1 = Sprint.builder()
            .id(1)
            .name("Sprint_1")
            .maxFeCapacity(10).build();
    private static final Sprint sprint2 = Sprint.builder()
            .id(2)
            .name("Sprint_2")
            .maxFeCapacity(10).build();

    ConstraintVerifier<TimeTableConstraintProvider, PiPlanning> constraintVerifier = ConstraintVerifier.build(
            new TimeTableConstraintProvider(), PiPlanning.class, UserStory.class);

    @Test
    void sprintFEConflict() {

        Feature feature1 = Feature.builder()
                .subject("Plain Vanilla")
                .build();
        Feature feature2 = Feature.builder()
                .subject("Table View New/Change")
                .build();

        UserStory firstUserStory = UserStory.builder()
                .id(1)
                .subject("FE: T1 - Reuse configuration form")
                .sprint(sprint1)
                .feCapacity(5)
                .feature(feature1)
                .build();

        UserStory conflictingUserStory = UserStory.builder()
                .id(2)
                .subject("FE: T2- Reuse configuration form")
                .sprint(sprint1)
                .feCapacity(10)
                .feature(feature1)
                .build();

        UserStory nonConflictingUserSTory = UserStory.builder()
                .id(3)
                .subject("FE: T3 - Reuse configuration form")
                .sprint(sprint2)
                .feCapacity(3)
                .feature(feature2)
                .build();

        UserStory US4 = UserStory.builder()
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
        Feature feature1 = Feature.builder()
                .subject("Feature with lowest priority")
                .priority(1)
                .build();
        Feature feature2 = Feature.builder()
                .subject("Feature with the highest priority")
                .priority(2)
                .build();


        UserStory us1 = UserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint1)
                .feCapacity(3)
                .feature(feature1)
                .build();

        UserStory us2 = UserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint1)
                .feCapacity(4)
                .feature(feature1)
                .build();

        UserStory us3 = UserStory.builder()
                .id(3)
                .subject("us3")
                .sprint(sprint2)
                .feCapacity(4)
                .feature(feature2)
                .build();

        UserStory us4 = UserStory.builder()
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
        Feature feature1 = Feature.builder()
                .id(1)
                .subject("Plain Vanilla")
                .priority(3)
                .build();
        Feature feature2 = Feature.builder()
                .id(2)
                .subject("Table View New/Change")
                .priority(1)
                .build();

        UserStory us1 = UserStory.builder()
                .id(1)
                .subject("us1")
                .sprint(sprint2)
                .feature(feature1)
                .build();
        UserStory us2 = UserStory.builder()
                .id(2)
                .subject("us2")
                .sprint(sprint1)
                .feature(feature2)
                .build();

        constraintVerifier.verifyThat(TimeTableConstraintProvider::featurePriority)
                .given(us1, us2)
                .penalizesBy(1);
    }


//
//    @Test
//    void teacherConflict() {
//        String conflictingTeacher = "Teacher1";
//        UserStory firstUserStory = new UserStory(1, "Subject1", conflictingTeacher, "Group1", TIMESLOT1, SPRINT_1);
//        UserStory conflictingUserStory = new UserStory(2, "Subject2", conflictingTeacher, "Group2", TIMESLOT1, SPRINT_2);
//        UserStory nonConflictingLesson = new UserStory(3, "Subject3", "Teacher2", "Group3", TIMESLOT2, SPRINT_1);
//        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherConflict)
//                .given(firstUserStory, conflictingUserStory, nonConflictingLesson)
//                .penalizesBy(1);
//    }
//
//    @Test
//    void studentGroupConflict() {
//        String conflictingGroup = "Group1";
//        UserStory firstUserStory = new UserStory(1, "Subject1", "Teacher1", conflictingGroup, TIMESLOT1, SPRINT_1);
//        UserStory conflictingUserStory = new UserStory(2, "Subject2", "Teacher2", conflictingGroup, TIMESLOT1, SPRINT_2);
//        UserStory nonConflictingLesson = new UserStory(3, "Subject3", "Teacher3", "Group3", TIMESLOT2, SPRINT_1);
//        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupConflict)
//                .given(firstUserStory, conflictingUserStory, nonConflictingLesson)
//                .penalizesBy(1);
//    }
//
//    @Test
//    void teacherRoomStability() {
//        String teacher = "Teacher1";
//        UserStory lessonInFirstRoom = new UserStory(1, "Subject1", teacher, "Group1", TIMESLOT1, SPRINT_1);
//        UserStory lessonInSameRoom = new UserStory(2, "Subject2", teacher, "Group2", TIMESLOT1, SPRINT_1);
//        UserStory lessonInDifferentRoom = new UserStory(3, "Subject3", teacher, "Group3", TIMESLOT1, SPRINT_2);
//        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherRoomStability)
//                .given(lessonInFirstRoom, lessonInDifferentRoom, lessonInSameRoom)
//                .penalizesBy(2);
//    }
//
//    @Test
//    void teacherTimeEfficiency() {
//        String teacher = "Teacher1";
//        UserStory singleLessonOnMonday = new UserStory(1, "Subject1", teacher, "Group1", TIMESLOT1, SPRINT_1);
//        UserStory firstTuesdayUserStory = new UserStory(2, "Subject2", teacher, "Group2", TIMESLOT2, SPRINT_1);
//        UserStory secondTuesdayLesson = new UserStory(3, "Subject3", teacher, "Group3", TIMESLOT3, SPRINT_1);
//        UserStory thirdTuesdayLessonWithGap = new UserStory(4, "Subject4", teacher, "Group4", TIMESLOT4, SPRINT_1);
//        constraintVerifier.verifyThat(TimeTableConstraintProvider::teacherTimeEfficiency)
//                .given(singleLessonOnMonday, firstTuesdayUserStory, secondTuesdayLesson, thirdTuesdayLessonWithGap)
//                .rewardsWith(1); // Second tuesday lesson immediately follows the first.
//    }
//
//    @Test
//    void studentGroupSubjectVariety() {
//        String studentGroup = "Group1";
//        String repeatedSubject = "Subject1";
//        UserStory mondayLesson = new UserStory(1, repeatedSubject, "Teacher1", studentGroup, TIMESLOT1, SPRINT_1);
//        UserStory firstTuesdayUserStory = new UserStory(2, repeatedSubject, "Teacher2", studentGroup, TIMESLOT2, SPRINT_1);
//        UserStory secondTuesdayLesson = new UserStory(3, repeatedSubject, "Teacher3", studentGroup, TIMESLOT3, SPRINT_1);
//        UserStory thirdTuesdayLessonWithDifferentSubject = new UserStory(4, "Subject2", "Teacher4", studentGroup, TIMESLOT4, SPRINT_1);
//        UserStory lessonInAnotherGroup = new UserStory(5, repeatedSubject, "Teacher5", "Group2", TIMESLOT1, SPRINT_1);
//        constraintVerifier.verifyThat(TimeTableConstraintProvider::studentGroupSubjectVariety)
//                .given(mondayLesson, firstTuesdayUserStory, secondTuesdayLesson, thirdTuesdayLessonWithDifferentSubject,
//                        lessonInAnotherGroup)
//                .penalizesBy(1); // Second tuesday lesson immediately follows the first.
//    }

}
