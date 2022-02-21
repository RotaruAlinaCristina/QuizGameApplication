package com.projectplus.quiz.model;

import java.util.Collection;
import java.util.EnumSet;
public class Difficulty {
    DifficultyGame difficultyGame;


    public static DifficultyGame calculateNextDifficulty(Collection<DifficultyGame> difficulties) {
        if (difficulties == null || difficulties.isEmpty()) {
            return null;
        }
        if (difficulties.size() == 1) {
            return difficulties.iterator().next().getClosestDifficulty();
        }
        EnumSet<DifficultyGame> missingDifficulties = EnumSet.complementOf(EnumSet.copyOf(difficulties));
        if (missingDifficulties.isEmpty()) {
            return null;
        } else {
            return missingDifficulties.iterator().next();
        }
    }
}
