package com.projectplus.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class HealthCheck {

    private boolean status;
    private String message;
}
