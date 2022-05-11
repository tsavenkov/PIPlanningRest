
package com.model.inputModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserStory {
    private Long beCapacity;
    private Long feCapacity;
    private Long sdCapacity;
    private String subject;
}
