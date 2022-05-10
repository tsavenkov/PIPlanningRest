package com.controller;

import com.model.outputModel.OutputModel;
import com.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class Controller {

    @Autowired
    Service service;

    @GetMapping("/solve")
    public OutputModel solve() {
        return service.solve();
    }
}

