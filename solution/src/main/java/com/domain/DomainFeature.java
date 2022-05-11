package com.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainFeature {
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
