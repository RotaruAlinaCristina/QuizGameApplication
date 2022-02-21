package com.projectplus.quiz.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.java.Log;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@NoArgsConstructor
@Getter
@ToString
@Log
public class Categories {

    @JsonProperty("trivia_categories")
    private List<Category> categories;


    @NoArgsConstructor
    @Getter
    @ToString
    public static class Category {
        private int id;
        private String name;
    }

    public void getQuizCategories() {
        RestTemplate restTemplate = new RestTemplate();
        Categories result = restTemplate.getForObject("https://opentdb.com/api_category.php", Categories.class);
        log.info("Quiz categories: " + result.getCategories());
    }




}
