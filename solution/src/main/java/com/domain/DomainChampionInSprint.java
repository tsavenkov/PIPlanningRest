package com.domain;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainChampionInSprint implements Serializable {

    @Getter
    @Setter
    private String name;      // references the id of the champion with the capacity specification

    @Getter
    @Setter
    private int capacity;   //amount

    @Getter
    @Setter
    private String capaType;   //fe be sd shared
}