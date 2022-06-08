package com.model.outputModel;

import com.domain.DomainFeature;
import lombok.Builder;
import lombok.Data;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import java.util.List;

@Data
@Builder
public class OutputModel {
    private static final long serialVersionUID = 1L;

    private HardMediumSoftScore score;
    private int totalUnusedFECapacity;
    private int totalUnusedBECapacity;
    private int totalUnusedSDCapacity;
    private int totalUnusedQACapacity;

    private int totalUndistributedFECapacity;
    private int totalUndistributedBECapacity;
    private int totalUndistributedSDCapacity;
    private int totalUndistributedQACapacity;
    private List<OutputSprint> outputSprintList;

    private List<DomainFeature> partiallyPlannedFeatures;
}
