package org.acme.schooltimetabling.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feature {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private int priority;
}
