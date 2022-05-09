package org.acme.piplanning.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feature {
    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private int priority;
}
