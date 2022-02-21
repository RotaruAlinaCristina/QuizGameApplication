package com.projectplus.quiz.model;

import lombok.Data;

@Data
public class GameOptions {
    private int numberOfQuestions = 5;
    private DifficultyGame difficulty;
    private int categoryId;
}
