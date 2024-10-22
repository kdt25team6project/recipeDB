package com.bestprice.bestprice_back.components.controller;

import com.bestprice.bestprice_back.components.entity.RecipeEntity;
import com.bestprice.bestprice_back.components.service.WebCrawlerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipes/crawl")
public class WebCrawlerController {

    private final WebCrawlerService webCrawlerService;

    public WebCrawlerController(WebCrawlerService webCrawlerService) {
        this.webCrawlerService = webCrawlerService;
    }

    @GetMapping
    public RecipeEntity crawlRecipe(@RequestParam int rcpSno) {
        return webCrawlerService.crawlRecipe(rcpSno);
    }
}
