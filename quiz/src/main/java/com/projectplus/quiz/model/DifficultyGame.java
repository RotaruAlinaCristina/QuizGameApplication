package com.projectplus.quiz.model;

public enum DifficultyGame {
    EASY,
    MEDIUM,
    HARD;

    public DifficultyGame getClosestDifficulty() {
        switch (this){
            case EASY:
                return MEDIUM;
            case MEDIUM:
                return HARD;
            case HARD:
                return MEDIUM;
        }
        return null;
    }

}
