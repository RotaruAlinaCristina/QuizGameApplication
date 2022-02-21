package com.projectplus.quiz.service;

import com.projectplus.quiz.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class QuizDataServiceTest {

    @Mock
    Logger log;
    @InjectMocks
    QuizDataService quizDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetQuizQuestions() {
        List<Questions.Question> result = quizDataService.getQuizQuestions(new GameOptions());
        assertEquals(Arrays.<Questions.Question>asList(new Questions.Question()), result);
    }

    @Test
    void testGetQuizCategories() {
        List<Categories.Category> result = quizDataService.getQuizCategories();
        assertEquals(Arrays.<Categories.Category>asList(new Categories.Category()), result);
    }

    @Test
    void testCalculateEachDifficultyQuestionCount() {
        Map<DifficultyGame, Integer> result = QuizDataService.calculateEachDifficultyQuestionCount(0, DifficultyGame.EASY, new CategoryQuestionCountInfo());
        assertEquals(new HashMap<DifficultyGame, Integer>() {{
            put(DifficultyGame.EASY, Integer.valueOf(0));
        }}, result);
    }

    @Test
    void calculateEachDifficultyQuestionCount_basicEasy() {
        CategoryQuestionCountInfo categoryQuestionCount = new CategoryQuestionCountInfo(5, 17, 13);
        Map<DifficultyGame, Integer> result = QuizDataService.calculateEachDifficultyQuestionCount(20, DifficultyGame.EASY, categoryQuestionCount);

        assertEquals(5, result.get(DifficultyGame.EASY));
        assertEquals(15, result.get(DifficultyGame.MEDIUM));
        assertNull(result.get(DifficultyGame.HARD));
    }


}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme