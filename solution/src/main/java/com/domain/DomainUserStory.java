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
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@AllArgsConstructor
@NoArgsConstructor
@PlanningEntity
@Builder
public class DomainUserStory {


    @Getter
    @Setter
    @PlanningId
    private int id;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private int feCapacity;

    @Getter
    @Setter
    private int beCapacity;

    @Getter
    @Setter
    private int befeTotalCapacity;

    @Getter
    @Setter
    private int sdCapacity;

    @Getter
    @Setter
    private DomainFeature feature;

    @Getter
    @Setter
    private long fixedSprint;


    @PlanningVariable(valueRangeProviderRefs = "sprintRange")
    private DomainSprint sprint;


    public DomainSprint getSprint() {
        return sprint;
    }

    public void setSprint(DomainSprint sprint) {
        this.sprint = sprint;
    }

    @Override
    public String toString() {
        return subject + "(" + id + ")";
    }
}
