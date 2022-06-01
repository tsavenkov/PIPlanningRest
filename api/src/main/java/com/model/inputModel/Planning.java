
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
public class Planning {

    private List<InputFeature> features;
    private List<InputSprint> sprints;

}
