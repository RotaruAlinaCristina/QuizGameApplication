package com.projectplus.quiz.service;

import com.projectplus.quiz.model.*;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Log
@Service
public class QuizDataService {

    public List<Questions.Question> getQuizQuestions(GameOptions gameOptions) {
        CategoryQuestionCountInfo categoryQuestionCount = getCategoryQuestionCount(gameOptions.getCategoryId());
        int availableQuestionCount = categoryQuestionCount.getQuestionCountForDifficulty(gameOptions.getDifficulty());
        if (availableQuestionCount >= gameOptions.getNumberOfQuestions()) {
            return getQuizQuestions(gameOptions.getNumberOfQuestions(), gameOptions.getCategoryId(), gameOptions.getDifficulty());
        } else {
            return getQuizQuestions(availableQuestionCount, gameOptions.getCategoryId(), gameOptions.getDifficulty());
        }
    }

    private List<Questions.Question> getQuizQuestions(int numberOfQuestions, int categoryId, DifficultyGame difficulty) {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder.fromHttpUrl("https://opentdb.com/api.php")
                .queryParam("amount", numberOfQuestions)
                .queryParam("category", categoryId)
                .queryParam("difficulty", difficulty.name().toLowerCase())
                .build().toUri();
        log.info("Quiz question retrieve URL: " + uri);

        Questions result = restTemplate.getForObject(uri, Questions.class);
        log.info("Quiz questions: Open Trivia DB response code = " + result.getResponseCode() + ". Content: " + result.getResults());
        return result.getResults();
    }

    private CategoryQuestionCountInfo getCategoryQuestionCount(int categoryId) {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder.fromHttpUrl("https://opentdb.com/api_count.php")
                .queryParam("category", categoryId)
                .build().toUri();
        log.info("Quiz category question count retrieve URL: " + uri);
        CategoryQuestionCountInfo result = restTemplate.getForObject(uri, CategoryQuestionCountInfo.class);
        log.info("Quiz category question count content: " + result);
        return result;
    }

//    public void getQuizCategories() {
//        RestTemplate restTemplate = new RestTemplate();
//        Categories result = restTemplate.getForObject("https://opentdb.com/api_category.php", Categories.class);
//         log.info("Quiz categories: " + result.getCategories());
//    }

    public List<Categories.Category> getQuizCategories() {
        RestTemplate restTemplate = new RestTemplate();
        Categories result = restTemplate.getForObject("https://opentdb.com/api_category.php", Categories.class);
        log.info("Quiz categories: " + result.getCategories());
        return result.getCategories();
    }

    static Map<DifficultyGame, Integer> calculateEachDifficultyQuestionCount(int numberOfQuestions, DifficultyGame difficulty, CategoryQuestionCountInfo categoryQuestionCount) {
        Map<DifficultyGame, Integer> eachDifficultyQuestionCount = new EnumMap<>(DifficultyGame.class);
        eachDifficultyQuestionCount.put(difficulty, Math.min(numberOfQuestions, categoryQuestionCount.getQuestionCountForDifficulty(difficulty)));

        int missingQuestions = numberOfQuestions - eachDifficultyQuestionCount.values().stream().mapToInt(i -> i).sum();
        while (missingQuestions > 0) {
            DifficultyGame closestDifficulty = Difficulty.calculateNextDifficulty(eachDifficultyQuestionCount.keySet());
            if (closestDifficulty == null) {
                log.warning("Not enough question in given category!");
                break;
            }
            eachDifficultyQuestionCount.put(closestDifficulty, Math.min(missingQuestions, categoryQuestionCount.getQuestionCountForDifficulty(closestDifficulty)));

            missingQuestions = numberOfQuestions - eachDifficultyQuestionCount.values().stream().mapToInt(i -> i).sum();
        }
        if (difficulty == DifficultyGame.MEDIUM) {
            DifficultyGame otherDifficulty = Difficulty.calculateNextDifficulty(eachDifficultyQuestionCount.keySet());
            if (otherDifficulty != null) {
                DifficultyGame filledDifficulty = difficulty.getClosestDifficulty();
                final int numberToTransfer = Math.min(eachDifficultyQuestionCount.get(filledDifficulty) / 2, categoryQuestionCount.getQuestionCountForDifficulty(otherDifficulty));
                eachDifficultyQuestionCount.computeIfPresent(filledDifficulty, (d, count) -> count - numberToTransfer);
                eachDifficultyQuestionCount.put(otherDifficulty, numberToTransfer);
            }

        }
        return eachDifficultyQuestionCount;
    }
}
