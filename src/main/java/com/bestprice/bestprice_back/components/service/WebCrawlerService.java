package com.bestprice.bestprice_back.components.service;

import com.bestprice.bestprice_back.components.entity.RecipeEntity;
import com.bestprice.bestprice_back.components.repository.RecipeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class WebCrawlerService {

    private final RecipeRepository recipeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public WebCrawlerService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public RecipeEntity crawlRecipe(int rcpSno) {
        String url = "https://www.10000recipe.com/recipe/" + rcpSno;

        // 이미 레시피가 존재하는지 확인
        if (recipeRepository.existsById(rcpSno)) {
            System.out.println("이미 존재하는 레시피입니다: " + rcpSno);
            return recipeRepository.findById(rcpSno).orElse(null);
        }

        RecipeEntity recipe = new RecipeEntity();
        recipe.setRcpSno(rcpSno);

        try {
            Document document = Jsoup.connect(url).get();

            // 메인 이미지 설정
            Element mainThumb = document.getElementById("main_thumbs");
            recipe.setMainThumb(mainThumb != null ? mainThumb.attr("src") : null);

            // 제목 설정
            Element titleElement = document.selectFirst("div.view2_summary h3");
            recipe.setTitle(titleElement != null ? titleElement.text() : "제목 없음");

            // 레시피 저장
            recipeRepository.save(recipe);
            System.out.println("레시피 저장 완료: " + rcpSno);

            // 단계 정보 저장
            saveSteps(rcpSno, document);

        } catch (IOException e) {
            System.err.println("크롤링 실패: " + e.getMessage());
        }

        return recipe;
    }

    private void saveSteps(int rcpSno, Document document) {
        Elements stepElements = document.select("div.view_step_cont");

        int order = 1;
        for (Element stepElement : stepElements) {
            String stepText = stepElement.text();
            if (!stepText.isEmpty()) {
                entityManager.createNativeQuery(
                        "INSERT INTO recipe_steps (rcp_sno, step, step_order) VALUES (:rcpSno, :step, :stepOrder)")
                        .setParameter("rcpSno", rcpSno)
                        .setParameter("step", stepText)
                        .setParameter("stepOrder", order++)
                        .executeUpdate();
            }
        }
        System.out.println("단계 정보 저장 완료: " + rcpSno);
    }

    // 새롭게 정의된 mergeCrawledData 메서드
    public void mergeCrawledData(RecipeEntity crawledRecipe, RecipeEntity csvRecipe) {
        if (crawledRecipe.getMainThumb() != null) {
            csvRecipe.setMainThumb(crawledRecipe.getMainThumb());
        }
        if (crawledRecipe.getTitle() != null) {
            csvRecipe.setTitle(crawledRecipe.getTitle());
        }
        if (crawledRecipe.getDescription() != null) {
            csvRecipe.setDescription(crawledRecipe.getDescription());
        }
        System.out.println("크롤링 데이터 병합 완료: " + csvRecipe.getRcpSno());
    }
}
