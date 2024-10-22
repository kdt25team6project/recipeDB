package com.bestprice.bestprice_back.components.controller;

import com.bestprice.bestprice_back.components.service.RecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipes/csv")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/load")
    public String loadCsvAndCrawlRecipes() {
        int count = recipeService.loadCsvAndCrawlRecipes();
        return count + "개의 레시피가 CSV에서 로드되고 크롤링되었습니다.";
    }
}
