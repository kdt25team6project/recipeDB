package com.bestprice.bestprice_back.components.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "recipes")
public class RecipeDto {

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

    // 재료 목록과의 관계 설정
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientDto> ingredientsList = new ArrayList<>();

    // 조리 단계와의 관계 설정
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StepDto> steps = new ArrayList<>();

    // 날짜 문자열을 LocalDateTime으로 변환하는 메서드
    public void setFirstRegDtFromString(String dateString) {
        this.firstRegDt = java.time.LocalDateTime.parse(
                dateString,
                java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
