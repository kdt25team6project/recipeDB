package com.bestprice.bestprice_back.components.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "recipe_ingredients")
@JsonIgnoreProperties("recipe")
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rcp_sno", referencedColumnName = "rcpSno", nullable = false)
    private RecipeEntity recipe;

    private String sectionName;
    private String name;
    private String amount;
}
