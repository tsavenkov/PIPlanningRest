package com.model.outputModel;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OutputModel {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String family_name;
    private String email;
    private String phone;
    private BigDecimal globalRating;
    private BigDecimal tournamentGameRating;
    private double tournamentPointRating;
    private BigDecimal tournamentPointWon;
    private BigDecimal tournamentPointLost;
}
