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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@AllArgsConstructor
@NoArgsConstructor
@PlanningEntity
public class Lesson {

    @PlanningId
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String teacher;

    @Getter
    @Setter
    private String studentGroup;

    @PlanningVariable(valueRangeProviderRefs = "timeslotRange")
    @Getter
    @Setter
    private PIPlanningSchedule timeslot;
    @PlanningVariable(valueRangeProviderRefs = "roomRange")
    @Getter
    @Setter
    private Room room;


    public Lesson(long id, String subject, String teacher, String studentGroup) {
        this.id = id;
        this.subject = subject;
        this.teacher = teacher;
        this.studentGroup = studentGroup;
    }

    public Lesson(long id, String subject, String teacher, String studentGroup, PIPlanningSchedule timeslot, Room room) {
        this(id, subject, teacher, studentGroup);
        this.timeslot = timeslot;
        this.room = room;
    }

    @Override
    public String toString() {
        return subject + "(" + id + ")";
    }
}
