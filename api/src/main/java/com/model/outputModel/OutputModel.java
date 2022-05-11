package com.model.outputModel;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OutputModel {
    private static final long serialVersionUID = 1L;

    private List<OutputSprint> outputSprintList;
}
