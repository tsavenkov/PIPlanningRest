package com.model.outputModel;

import com.domain.DomainFeature;
import lombok.Builder;
import lombok.Data;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@Data
@Builder
public class OutputModel {
    private static final long serialVersionUID = 1L;

    private HardSoftScore score;
    private int totalUnusedFECapacity;
    private int totalUnusedBECapacity;
    private int totalUnusedSDCapacity;

    private int totalUndistributedFECapacity;
    private int totalUndistributedBECapacity;
    private int totalUndistributedSDCapacity;
    private List<OutputSprint> outputSprintList;

    private List<DomainFeature> partiallyPlannedFeatures;
}
