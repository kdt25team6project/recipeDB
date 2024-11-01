package com.bestprice.bestprice_back.components.repository;

import com.bestprice.bestprice_back.components.domain.RecipeDto;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<RecipeDto, Integer> {
    Optional<RecipeDto> findByRcpSno(Integer rcpSno);

}
