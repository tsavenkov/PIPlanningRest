
package com.model.outputModel;

import com.domain.DomainUserStory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OutputSprint {

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

    private String name;
    private List<DomainUserStory> userStoryList;
}
