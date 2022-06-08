
package com.model.outputModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OutputUserStory {

    private Long id;
    private String featureName;
    private String Sprint1name;
    private String Sprint2name;
    private String Sprint3name;
    private String Sprint4name;
    private String Sprint5name;
    private String Sprint6name;          //with id = 7
    private String outOfScopeName;        //always with id = 6
    private int capacity;
}
