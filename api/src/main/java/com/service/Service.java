package com.service;

import com.model.outputModel.OutputModel;

@org.springframework.stereotype.Service
public class Service {

    public OutputModel solve() {
        return OutputModel.builder().build();
    }
}
