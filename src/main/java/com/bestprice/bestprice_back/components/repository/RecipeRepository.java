package com.bestprice.bestprice_back.components.repository;

import com.bestprice.bestprice_back.components.entity.RecipeEntity;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<RecipeEntity, Integer> {
    Optional<RecipeEntity> findByRcpSno(Integer rcpSno);
}
