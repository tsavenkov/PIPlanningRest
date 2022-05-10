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

package com.solver;

import com.domain.UserStory;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.*;

public class TimeTableConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                //   Hard constraints
                feStoryPointsConflictTotal(constraintFactory),
                featurePriority(constraintFactory),
                beStoryPointsConflictTotal(constraintFactory),
                sdStoryPointsConflictTotal(constraintFactory)
                //
        };

//        return new Constraint[]{
//                // Hard constraints
//                roomConflict(constraintFactory),
//                teacherConflict(constraintFactory),
//                studentGroupConflict(constraintFactory),
//                // Soft constraints
//                teacherRoomStability(constraintFactory),
//                teacherTimeEfficiency(constraintFactory),
//                studentGroupSubjectVariety(constraintFactory)
//        };
    }

    Constraint feStoryPointsConflictTotal(ConstraintFactory constraintFactory) {
        // fe story points for spring can not be more than total sprint fe capacity.
        return constraintFactory.forEach(UserStory.class)
                .groupBy(UserStory::getSprint, ConstraintCollectors.sum(UserStory::getFeCapacity))
                .filter((sprint, totalFECapacity) -> totalFECapacity > sprint.getMaxFeCapacity())
                .penalize("FE story points conflict", HardSoftScore.ONE_HARD,
                        (sprint, totalFECapacity) -> totalFECapacity - sprint.getMaxFeCapacity());
    }

    Constraint beStoryPointsConflictTotal(ConstraintFactory constraintFactory) {
        // fe story points for spring can not be more than total sprint fe capacity.
        return constraintFactory.forEach(UserStory.class)
                .groupBy(UserStory::getSprint, ConstraintCollectors.sum(UserStory::getBeCapacity))
                .filter((sprint, totalBECapacity) -> totalBECapacity > sprint.getMaxBeCapacity())
                .penalize("BE story points conflict", HardSoftScore.ONE_HARD,
                        (sprint, totalBECapacity) -> totalBECapacity - sprint.getMaxBeCapacity());
    }

    Constraint sdStoryPointsConflictTotal(ConstraintFactory constraintFactory) {
        // fe story points for spring can not be more than total sprint fe capacity.
        return constraintFactory.forEach(UserStory.class)
                .groupBy(UserStory::getSprint, ConstraintCollectors.sum(UserStory::getSdCapacity))
                .filter((sprint, totalSDCapacity) -> totalSDCapacity > sprint.getMaxSdCapacity())
                .penalize("SD story points conflict", HardSoftScore.ONE_HARD,
                        (sprint, totalSDCapacity) -> totalSDCapacity - sprint.getMaxSdCapacity());
    }

    Constraint featurePriority(ConstraintFactory constraintFactory) {
        // the feature with the higher priority should go to the earlierst sprint     . Penalty is 1 for each violation
        return constraintFactory
                .forEach(UserStory.class)
                .join(UserStory.class, Joiners.lessThan(UserStory::getId))
                .filter((us1, us2) -> (
                        (us1.getFeature().getPriority() < us2.getFeature().getPriority() && us1.getSprint().getId() < us2.getSprint().getId()) ||
                                (us1.getFeature().getPriority() > us2.getFeature().getPriority() && us1.getSprint().getId() > us2.getSprint().getId())))
                .penalize("Feature priority", HardSoftScore.ONE_SOFT, (us1, us2) -> 1);
    }


    //    Constraint roomConflict(ConstraintFactory constraintFactory) {
//        // A room can accommodate at most one lesson at the same time.
//        return constraintFactory
//                // Select each pair of 2 different lessons ...
//                .forEachUniquePair(UserStory.class,
//                        // ... in the same timeslot ...
//                        Joiners.equal(UserStory::getTimeslot),
//                        // ... in the same room ...
//                        Joiners.equal(UserStory::getSprint))
//                // ... and penalize each pair with a hard weight.
//                .penalize("Room conflict", HardSoftScore.ONE_HARD);
//    }
//
//    Constraint teacherConflict(ConstraintFactory constraintFactory) {
//        // A teacher can teach at most one lesson at the same time.
//        return constraintFactory
//                .forEachUniquePair(UserStory.class,
//                        Joiners.equal(UserStory::getTimeslot),
//                        Joiners.equal(UserStory::getTeacher))
//                .penalize("Teacher conflict", HardSoftScore.ONE_HARD);
//    }
//
//    Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
//        // A student can attend at most one lesson at the same time.
//        return constraintFactory
//                .forEachUniquePair(UserStory.class,
//                        Joiners.equal(UserStory::getTimeslot),
//                        Joiners.equal(UserStory::getStudentGroup))
//                .penalize("Student group conflict", HardSoftScore.ONE_HARD);
//    }
//

//
//    Constraint teacherTimeEfficiency(ConstraintFactory constraintFactory) {
//        // A teacher prefers to teach sequential lessons and dislikes gaps between lessons.
//        return constraintFactory
//                .forEach(UserStory.class)
//                .join(UserStory.class, Joiners.equal(UserStory::getTeacher),
//                        Joiners.equal((lesson) -> lesson.getTimeslot().getDayOfWeek()))
//                .filter((lesson1, lesson2) -> {
//                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
//                            lesson2.getTimeslot().getStartTime());
//                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
//                })
//                .reward("Teacher time efficiency", HardSoftScore.ONE_SOFT);
//    }
//
//    Constraint studentGroupSubjectVariety(ConstraintFactory constraintFactory) {
//        // A student group dislikes sequential lessons on the same subject.
//        return constraintFactory
//                .forEach(UserStory.class)
//                .join(UserStory.class,
//                        Joiners.equal(UserStory::getSubject),
//                        Joiners.equal(UserStory::getStudentGroup),
//                        Joiners.equal((lesson) -> lesson.getTimeslot().getDayOfWeek()))
//                .filter((lesson1, lesson2) -> {
//                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
//                            lesson2.getTimeslot().getStartTime());
//                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
//                })
//                .penalize("Student group subject variety", HardSoftScore.ONE_SOFT);
//    }

}
