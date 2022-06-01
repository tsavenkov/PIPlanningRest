package com.domain;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainChampion implements Serializable {

    @Getter
    @Setter
    private String name;
}
