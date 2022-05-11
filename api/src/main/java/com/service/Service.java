package com.service;

import com.TimeTableApp;
import com.domain.PiPlanning;
import com.mapper.InputToProblem;
import com.model.inputModel.Planning;

@org.springframework.stereotype.Service
public class Service {

    public PiPlanning solve(Planning inputPlanning) {
        PiPlanning piPlanning = InputToProblem.mapInputToProblem(inputPlanning);
        
        return TimeTableApp.solve(piPlanning);
    }
}
