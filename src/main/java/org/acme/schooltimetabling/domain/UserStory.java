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

package org.acme.schooltimetabling.domain;

import lombok.*;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@AllArgsConstructor
@NoArgsConstructor
@PlanningEntity
@Builder
public class UserStory {

    @PlanningId
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private Integer feCapacity;

    @Getter
    @Setter
    private Integer beCapacity;

    @Getter
    @Setter
    private Integer sdCapacity;

    @Getter
    @Setter
    private Feature feature;


    @PlanningVariable(valueRangeProviderRefs = "sprintRange")
    public Sprint getSprint() {
        return sprint;
    }
    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    private Sprint sprint;



    @Override
    public String toString() {
        return subject + "(" + id + ")";
    }
}
