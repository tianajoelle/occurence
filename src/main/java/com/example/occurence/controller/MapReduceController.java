package com.example.occurence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.occurence.service.AkkaService;

@Controller
@RequestMapping("/occurence")
public class MapReduceController {

    @Autowired
    private AkkaService service;

    @PostMapping("/init")
    public String initializeActors() {
        service.create();
        return "home";
    }
    

    @GetMapping("/home")
    public String home() {
        return "home"; 
    }
}
