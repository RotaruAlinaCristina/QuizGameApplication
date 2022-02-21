package com.projectplus.quiz.controller;

import com.projectplus.quiz.model.HealthCheck;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    @GetMapping
    public HealthCheck healthCheck(){
        return new HealthCheck(true, "It's working!");
    }
}
