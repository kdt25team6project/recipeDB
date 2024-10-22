package com.bestprice.bestprice_back.components.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "recipe_steps")
public class StepEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepId;

    @Column(name = "rcp_sno", nullable = false)
    private Integer rcpSno;

    @Column(length = 1000, nullable = false)
    private String step;

    @Column(nullable = false)
    private int stepOrder;
}
