
package com.model.inputModel;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserStory {
    private Long beCapacity;
    private Long feCapacity;
    private Long sdCapacity;
    private Long qaCapacity;
    private String subject;
    private Long fixedSprint;
}
