package com.projectplus.quiz.service;

import com.projectplus.quiz.model.DifficultyGame;
import com.projectplus.quiz.model.GameOptions;
import com.projectplus.quiz.model.Questions;
import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope
@Log
public class OngoingGameService {
    private GameOptions gameOptions;
    private int currentQuestionIndex;
    @Getter
    private int points;

    private List<Questions.Question> questions;

    @Autowired
    private QuizDataService quizDataService;

    //for the button Play
    public void init(GameOptions gameOptions) {
        this.gameOptions = gameOptions;
        this.currentQuestionIndex = 0;
        this.points = 0;

       this.questions = quizDataService.getQuizQuestions(gameOptions);
    }

    public int getCurrentQuestionNumber() {
        return currentQuestionIndex+1;
    }

    public int getTotalQuestionNumber() {
        return questions.size();
    }

    public String getCurrentQuestion() {
        Questions.Question dto = questions.get(currentQuestionIndex);
        return dto.getQuestion();
    }

    public List<String> getCurrentQuestionAnswersInRandomOrder() {
        Questions.Question dto = questions.get(currentQuestionIndex);

        List<String> answers = new ArrayList<>();
        answers.add(dto.getCorrectAnswer());
        answers.addAll(dto.getIncorrectAnswers());

        //changing the order of the items random
        Collections.shuffle(answers);
        return answers;
    }

    public boolean checkAnswerForCurrentQuestionAndUpdatePoints(String userAnswer) {
        Questions.Question dto = questions.get(currentQuestionIndex);
        boolean correct = dto.getCorrectAnswer().equals(userAnswer);
        if (correct) {
            points++;
        }
        return correct;
    }

    //true if we can ask another question and false if there are no more questions to ask - quiz finished
    public boolean proceedToNextQuestion() {
        currentQuestionIndex++;
        return currentQuestionIndex < questions.size();
    }

    public DifficultyGame getDifficulty() {
        return gameOptions.getDifficulty();
    }

    public String getCategoryName() {
        Optional<String> category = quizDataService.getQuizCategories().stream()
                .filter(categoryDto -> categoryDto.getId() == gameOptions.getCategoryId())
                .map(categoryDto -> categoryDto.getName())
                .findAny();
        return category.orElse(null);
    }

}
