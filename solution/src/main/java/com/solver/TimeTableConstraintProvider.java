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

import com.domain.DomainUserStory;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.*;

public class TimeTableConstraintProvider implements ConstraintProvider {
    int delta = 2;

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                //   Hard constraints
                feStoryPointsConflictTotal(constraintFactory),
                fixedSprint(constraintFactory),
                beStoryPointsConflictTotal(constraintFactory),
                sdStoryPointsConflictTotal(constraintFactory),
                featurePriority(constraintFactory),
                sprintCapacityUsage(constraintFactory)
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

    public Constraint feStoryPointsConflictTotal(ConstraintFactory constraintFactory) {
        // fe story points for spring can not be more than total sprint fe capacity.
        return constraintFactory.forEach(DomainUserStory.class)
                .groupBy(DomainUserStory::getSprint, ConstraintCollectors.sum(DomainUserStory::getFeCapacity))
                .filter((sprint, totalFECapacity) -> totalFECapacity > sprint.getMaxFeCapacity() + delta)
                .penalize("FE story points conflict", HardSoftScore.ONE_HARD,
                        (sprint, totalFECapacity) -> totalFECapacity - sprint.getMaxFeCapacity());
    }

    public Constraint beStoryPointsConflictTotal(ConstraintFactory constraintFactory) {
        // fe story points for spring can not be more than total sprint fe capacity.
        return constraintFactory.forEach(DomainUserStory.class)
                .groupBy(DomainUserStory::getSprint, ConstraintCollectors.sum(DomainUserStory::getBeCapacity))
                .filter((sprint, totalBECapacity) -> totalBECapacity > sprint.getMaxBeCapacity() + delta)
                .penalize("BE story points conflict", HardSoftScore.ONE_HARD,
                        (sprint, totalBECapacity) -> totalBECapacity - sprint.getMaxBeCapacity());
    }

    public Constraint sdStoryPointsConflictTotal(ConstraintFactory constraintFactory) {
        // fe story points for spring can not be more than total sprint fe capacity.
        return constraintFactory.forEach(DomainUserStory.class)
                .groupBy(DomainUserStory::getSprint, ConstraintCollectors.sum(DomainUserStory::getSdCapacity))
                .filter((sprint, totalSDCapacity) -> totalSDCapacity > sprint.getMaxSdCapacity() + delta)
                .penalize("SD story points conflict", HardSoftScore.ONE_HARD,
                        (sprint, totalSDCapacity) -> totalSDCapacity - sprint.getMaxSdCapacity());
    }

    public Constraint featurePriority(ConstraintFactory constraintFactory) {
        // the feature with the higher priority should go to the earlierst sprint     . Penalty is 1 for each violation
        // we exclude from viaolation the case when feature is in Sprint 6 - it means its out of scope (because of hard constraints) and thats why we dont penalise that for ex. us with lower prio is in sprint 5 and
        // user story with higher prio is in sprint 6

        // this should also not include the feature with the fixed sprint
        return constraintFactory
                .forEach(DomainUserStory.class)
                .join(DomainUserStory.class, Joiners.lessThan(DomainUserStory::getId))
                .filter((us1, us2) -> (
                        (us1.getFeature().getPriority() < us2.getFeature().getPriority() && us1.getSprint().getId() < us2.getSprint().getId() && us2.getSprint().getId() != 6 && us2.getFixedSprint()==0) ||
                                (us1.getFeature().getPriority() > us2.getFeature().getPriority() && us1.getSprint().getId() > us2.getSprint().getId() && us1.getSprint().getId() != 6) && us1.getFixedSprint()==0))
                .penalize("Feature priority", HardSoftScore.ONE_SOFT, (us1, us2) -> 1);
    }

    public Constraint sprintCapacityUsage(ConstraintFactory constraintFactory) {
        // this constraint makes sure that sprint can not contain unused capacity that covers some user stories which are in sprint 6
        // we do trick here: we penalize every user story which is in  out of scope sprint 6 with the soft penalty. This automatically place  user story to the earlier sprint if it fits to the capacity hard constraint
        return constraintFactory
                .forEach(DomainUserStory.class)
                .filter((us1) -> (us1.getSprint().getId() == 6))
                .penalize("Out of scope: user story placed to sprint 6", HardSoftScore.ONE_SOFT, us1 -> 1);
    }

    public Constraint fixedSprint(ConstraintFactory constraintFactory) {
        // if sprint is fixed for specific user story then we place it there with the hard constraint
        return constraintFactory
                .forEach(DomainUserStory.class).filter(domainUserStory -> (domainUserStory.getFixedSprint() != domainUserStory.getSprint().getId()) && domainUserStory.getFixedSprint() != 0)
                .penalize("Fixed sprint constraint violated", HardSoftScore.ONE_HARD, domainUserStory -> 1);

//                    .join(DomainUserStory.class, Joiners.equal(DomainUserStory::getId))
//                    .filter((us1, us2) -> (us1.getSprint().getId() == us1.getFixedSprint().longValue()) && us1.getFixedSprint()!=0)
//                    .penalize("Fixed sprint constraint violated", HardSoftScore.ONE_HARD, (us1, us2) -> 1);
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
