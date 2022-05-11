package com.service;

import com.TimeTableApp;
import com.domain.PiPlanning;
import com.mapper.InputToProblem;
import com.mapper.SolutionToOutputModel;
import com.model.inputModel.Planning;
import com.model.outputModel.OutputModel;

@org.springframework.stereotype.Service
public class Service {

    public PiPlanning solve(Planning inputPlanning) {
        PiPlanning piPlanning = InputToProblem.mapInputToProblem(inputPlanning);
        return TimeTableApp.solve(piPlanning);
    }

    public OutputModel solveWithTransform(Planning inputPlanning) {
        PiPlanning solution = solve(inputPlanning);
        return SolutionToOutputModel.mapSolutionToOutputSprint(solution);
    }

}
