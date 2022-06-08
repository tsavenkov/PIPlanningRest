
package com.model.outputModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class USCapacity {

    private Long id;

    private int maxFeCapacity;
    private int usedFeCapacity;
    private int nonUsedFeCapacity;

    private int maxBeCapacity;
    private int usedBeCapacity;
    private int nonUsedBeCapacity;

    private int maxSdCapacity;
    private int usedSdCapacity;
    private int nonUsedSdCapacity;

    private int maxQACapacity;
    private int usedQACapacity;
    private int nonUsedQACapacity;

    private int sharedCapacity;
    private int usedSharedCapacity;
    private int nonUsedSharedCapacity;
}
