
package com.model.inputModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Sprint {

    private Long id;
    private Long maxBeCapacity;
    private Long maxFeCapacity;
    private Long maxSdCapacity;
    private Long maxQaCapacity;
    private Long sharedCapacity;    //can be used for both Fe and Be.
    private String name;
}
