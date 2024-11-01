package com.bestprice.bestprice_back.components.domain;

import lombok.Data;

@Data
public class ProductDto {
    private String productName;
    private String price;
    private String imgUrl;
    private String link;
    private String productId;
    private String discount;
    private String basePrice;
}
