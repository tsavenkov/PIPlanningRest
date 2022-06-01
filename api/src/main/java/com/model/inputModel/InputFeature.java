
package com.model.inputModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InputFeature {

    private Long id;
    private Long priority;
    private String subject;
    private List<InputUserStory> userStories;

}
