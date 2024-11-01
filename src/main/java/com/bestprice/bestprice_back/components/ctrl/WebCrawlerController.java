package com.bestprice.bestprice_back.components.ctrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bestprice.bestprice_back.components.domain.ProductDto;
import com.bestprice.bestprice_back.components.domain.RecipeDto;
import com.bestprice.bestprice_back.components.domain.ShopSearchDto;
import com.bestprice.bestprice_back.components.service.WebCrawlerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class WebCrawlerController {

    @Autowired
    private WebCrawlerService webCrawlerService;

    @GetMapping("/recipes/crawl")
    public RecipeDto crawlRecipe(@RequestParam int rcpSno) {
        return webCrawlerService.crawlRecipe(rcpSno);
    }

    @GetMapping("/shop")
    public List<ShopSearchDto> shop(@RequestParam String query) {
        System.out.println("Query: " + query);

        List<ShopSearchDto> shopResults = new ArrayList<>();
        StringBuilder output = new StringBuilder();

        try {
            // Python Flask 서버에 HTTP GET 요청 보내기
            URL url = new URL("http://localhost:5000/search?keyword=" + URLEncoder.encode(query, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line);
                    }
                }

                ObjectMapper objectMapper = new ObjectMapper();
                List<ProductDto> products = objectMapper.readValue(output.toString(),
                        new TypeReference<List<ProductDto>>() {
                        });
                for (ProductDto product : products) {
                    ShopSearchDto dto = new ShopSearchDto();
                    dto.setProductId(product.getProductId()); // ID 변환
                    dto.setPrice(product.getPrice()); // 가격 변환
                    dto.setLink(product.getLink());
                    dto.setProductName(product.getProductName());
                    dto.setImgUrl(product.getImgUrl());
                    dto.setBasePrice(product.getBasePrice());
                    dto.setDiscount(product.getDiscount());

                    // 필요한 경우 추가 속성 설정
                    // dto.setDescription(...);
                    // dto.setCategory(...);
                    // dto.setSubCategory(...);
                    // dto.setDiscount(...);
                    // dto.setRecipeId(...);

                    shopResults.add(dto);
                }

            } else {
                System.err.println("서버 응답 오류: " + responseCode);
            }

        } catch (IOException e) {
            System.err.println("IO 오류: " + e.getMessage());
            e.printStackTrace();
        }

        return shopResults;
    }

}
