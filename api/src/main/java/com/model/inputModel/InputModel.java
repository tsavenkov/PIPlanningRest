package com.model.inputModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InputModel {
    private static final long serialVersionUID = 1L;

    private long gameID;
    private long tournamentId;

}
