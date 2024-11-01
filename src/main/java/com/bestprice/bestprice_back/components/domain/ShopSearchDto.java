package com.bestprice.bestprice_back.components.domain;

import lombok.Data;

@Data
public class ShopSearchDto {

    private String productId; // 상품 번호

    private String basePrice; // 원래 가격

    private String price; // 판매가

    private String link; // 상품 주소

    private String productName; // 상품 이름

    private String description; // 상세 설명

    private String imgUrl; // 상품 이미지url

    private String category; // 카테고리

    private String subCategory; // 세부분야

    private String discount; // 할인율

    private String recipeId; // 레시피 id

}
