
package com.model.inputModel;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InputUserStory {
    private Long beCapacity;
    private Long feCapacity;
    private Long sdCapacity;
    private String subject;
    private Long fixedSprint;
}
