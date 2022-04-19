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

package org.acme.schooltimetabling.solver;

import org.acme.schooltimetabling.domain.PiPlanning;
import org.acme.schooltimetabling.domain.UserStory;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;

class TimeTableConstraintProviderTest {

//    private static final Sprint SPRINT_1 = new Sprint("Room1");
//    private static final Sprint SPRINT_2 = new Sprint("Room2");
//    private static final Slot TIMESLOT1 = new Slot(DayOfWeek.MONDAY, LocalTime.NOON);
//    private static final Slot TIMESLOT2 = new Slot(DayOfWeek.TUESDAY, LocalTime.NOON);
//    private static final Slot TIMESLOT3 = new Slot(DayOfWeek.TUESDAY, LocalTime.NOON.plusHours(1));
//    private static final Slot TIMESLOT4 = new Slot(DayOfWeek.TUESDAY, LocalTime.NOON.plusHours(3));

    ConstraintVerifier<TimeTableConstraintProvider, PiPlanning> constraintVerifier = ConstraintVerifier.build(
            new TimeTableConstraintProvider(), PiPlanning.class, UserStory.class);

//    @Test
//    void roomConflict() {
//        UserStory firstUserStory = new UserStory(1, "Subject1", "Teacher1", "Group1", TIMESLOT1, SPRINT_1);
//        UserStory conflictingUserStory = new UserStory(2, "Subject2", "Teacher2", "Group2", TIMESLOT1, SPRINT_1);
//        UserStory nonConflictingLesson = new UserStory(3, "Subject3", "Teacher3", "Group3", TIMESLOT2, SPRINT_1);
//        constraintVerifier.verifyThat(TimeTableConstraintProvider::roomConflict)
//                .given(firstUserStory, conflictingUserStory, nonConflictingLesson)
//                .penalizesBy(1);
//    }
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
