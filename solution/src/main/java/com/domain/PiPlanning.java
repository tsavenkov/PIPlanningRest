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

package com.domain;

import lombok.*;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PiPlanning {

    private HardSoftScore score;
    private List<DomainSprint> sprintList;

    @Getter
    @Setter
    private List<DomainFeature> featureList;
    private List<DomainUserStory> userStoryList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "sprintRange")
    public List<DomainSprint> getSprintList() {
        return sprintList;
    }

    public void setSprintList(List<DomainSprint> sprintList) {
        this.sprintList = sprintList;
    }


    @PlanningEntityCollectionProperty
    public List<DomainUserStory> getUserStoryList() {
        return userStoryList;
    }

    public void setUserStoryList(List<DomainUserStory> userStoryList) {
        this.userStoryList = userStoryList;
    }


    @PlanningScore
    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }


}
