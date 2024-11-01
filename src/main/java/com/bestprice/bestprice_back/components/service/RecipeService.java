package com.bestprice.bestprice_back.components.service;

import com.bestprice.bestprice_back.components.domain.IngredientDto;
import com.bestprice.bestprice_back.components.domain.RecipeDto;
import com.bestprice.bestprice_back.components.repository.RecipeRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final WebCrawlerService webCrawlerService;

    // ID로 레시피 조회
    public RecipeDto getRecipeById(Integer id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다: " + id));
    }

    public RecipeService(RecipeRepository recipeRepository, WebCrawlerService webCrawlerService) {
        this.recipeRepository = recipeRepository;
        this.webCrawlerService = webCrawlerService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int loadCsvAndCrawlRecipes() {
        List<RecipeDto> recipes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("TB_RECIPE_SEARCH-20231130.csv").getInputStream(),
                StandardCharsets.UTF_8))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    String[] fields = line.split(",");
                    if (fields.length < 18) {
                        System.err.println("잘못된 레코드 형식: " + line);
                        continue;
                    }

                    // CSV에서 레시피 파싱
                    RecipeDto recipe = parseRecipeFromCsv(fields);
                    if (recipe == null) {
                        System.err.println("CSV 레코드 파싱 실패: " + line);
                        continue;
                    }

                    Optional<RecipeDto> existingRecipe = recipeRepository.findByRcpSno(recipe.getRcpSno());
                    if (existingRecipe.isPresent()) {
                        System.out.println("이미 존재하는 레시피: " + recipe.getRcpSno());
                        continue;
                    }

                    // 크롤링된 데이터와 병합
                    RecipeDto crawledRecipe = webCrawlerService.crawlRecipe(recipe.getRcpSno());
                    webCrawlerService.mergeCrawledData(crawledRecipe, recipe);

                    // 재료 정보 파싱 및 추가
                    parseSectionedIngredients(recipe, fields[13].trim());

                    recipes.add(recipe);
                    System.out.println("레시피 저장 준비 완료: " + recipe.getRcpSno());

                } catch (Exception e) {
                    System.err.println("레코드 처리 중 오류 발생:");
                    e.printStackTrace();
                    throw new RuntimeException("레코드 처리 실패", e);
                }
            }

            // 모든 레시피 저장
            saveRecipes(recipes);

        } catch (Exception e) {
            System.err.println("CSV 처리 중 오류 발생:");
            e.printStackTrace();
            throw new RuntimeException("CSV 처리 실패", e);
        }

        return recipes.size();
    }

    private RecipeDto parseRecipeFromCsv(String[] fields) {
        try {
            RecipeDto recipe = new RecipeDto();
            recipe.setRcpSno(Integer.parseInt(fields[0].trim()));
            recipe.setTitle(fields[1].trim());
            recipe.setCkgNm(fields[2].trim());
            recipe.setRgtrId(fields[3].trim());
            recipe.setRgtrNm(fields[4].trim());
            recipe.setInqCnt(Integer.parseInt(fields[5].trim()));
            recipe.setRcmmCnt(Integer.parseInt(fields[6].trim()));
            recipe.setSrapCnt(Integer.parseInt(fields[7].trim()));
            recipe.setCategory1(fields[8].trim());
            recipe.setCategory2(fields[9].trim());
            recipe.setCategory3(fields[10].trim());
            recipe.setCategory4(fields[11].trim());
            recipe.setDescription(fields[12].trim());
            recipe.setServings(fields[14].trim());
            recipe.setDifficulty(fields[15].trim());
            recipe.setTimeRequired(fields[16].trim());
            recipe.setFirstRegDtFromString(fields[17].trim());
            return recipe;
        } catch (NumberFormatException e) {
            System.err.println("숫자 형식 오류 발생: " + e.getMessage());
            return null;
        }
    }

    private void parseSectionedIngredients(RecipeDto recipe, String ingredientsString) {
        String[] sections = ingredientsString.split("\\[");

        for (String section : sections) {
            if (section.trim().isEmpty())
                continue;

            String[] sectionParts = section.split("\\]", 2);
            String sectionName = sectionParts[0].trim();
            String[] items = sectionParts[1].split("\\|");

            for (String item : items) {
                String[] details = item.trim().split(" ", 2);
                String name = details[0].trim();
                String amount = details.length > 1 ? details[1].trim() : null;

                IngredientDto ingredient = new IngredientDto();
                ingredient.setRecipe(recipe);
                ingredient.setSectionName(sectionName);
                ingredient.setName(name);
                ingredient.setAmount(amount);

                recipe.getIngredientsList().add(ingredient);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveRecipes(List<RecipeDto> recipes) {
        if (recipes.isEmpty()) {
            System.err.println("저장할 레시피가 없습니다.");
            return;
        }

        try {
            recipeRepository.saveAll(recipes);
            System.out.println("레시피 저장 성공: " + recipes.size() + "개");
        } catch (Exception e) {
            System.err.println("레시피 저장 중 오류 발생:");
            e.printStackTrace();
            throw new RuntimeException("레시피 저장 실패", e);
        }
    }

}
