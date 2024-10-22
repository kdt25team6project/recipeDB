package com.bestprice.bestprice_back.components.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "recipes")
public class RecipeEntity {

    @Id
    private Integer rcpSno;
    private String title;
    private String ckgNm;
    private String rgtrId;
    private String rgtrNm;
    private Integer inqCnt = 0;
    private Integer rcmmCnt = 0;
    private Integer srapCnt = 0;
    private String category1;
    private String category2;
    private String category3;
    private String category4;
    private String description;
    private String servings;
    private String difficulty;
    private String timeRequired;
    private String mainThumb;
    private java.time.LocalDateTime firstRegDt;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientEntity> ingredientsList = new ArrayList<>();

    public void setFirstRegDtFromString(String dateString) {
        this.firstRegDt = java.time.LocalDateTime.parse(dateString,
                java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
