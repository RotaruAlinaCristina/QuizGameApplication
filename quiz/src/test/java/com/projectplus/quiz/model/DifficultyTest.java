package com.projectplus.quiz.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumSet;

class DifficultyTest {
    //Field difficultyGame of type DifficultyGame - was not mocked since Mockito doesn't mock enums

    @Test
    void testCalculateNextDifficulty() {
        DifficultyGame result = Difficulty.calculateNextDifficulty(Arrays.<DifficultyGame>asList(DifficultyGame.EASY));
        Assertions.assertEquals(DifficultyGame.MEDIUM, result);
    }
}



//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme