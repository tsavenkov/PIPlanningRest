package com.controller;

import com.domain.PiPlanning;
import com.google.gson.Gson;
import com.model.inputModel.Planning;
import com.model.outputModel.OutputModel;
import com.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    @Autowired
    Service service;

    @PostMapping("/solveWithTransform")
    public OutputModel solveWithTransform(@RequestBody String planning) {
        Gson gson = new Gson();
        Planning planningParsed = gson.fromJson(planning, Planning.class);

        return service.solveWithTransform(planningParsed);
    }


    @PostMapping("/solve")
    public PiPlanning solve(@RequestBody String planning) {
        Gson gson = new Gson();
        Planning planningParsed = gson.fromJson(planning, Planning.class);

        return service.solve(planningParsed);
    }
}

