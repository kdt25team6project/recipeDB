package com.bestprice.bestprice_back.components.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "recipe_steps")
public class StepEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepId;

    @ManyToOne
    @JsonIgnoreProperties("recipe")
    @JoinColumn(name = "rcp_sno", nullable = false)
    @JsonBackReference
    private RecipeEntity recipe;

    @Column(length = 1000, nullable = false)
    private String step;

    @Column(nullable = false)
    private int stepOrder;

    @Column(name = "step_img")
    private String stepImg;
}
