package com.smartgrocery.repository

import com.smartgrocery.entity.RecipeIngredient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeIngredientRepository : JpaRepository<RecipeIngredient, Long> {
    
    fun findByRecipeId(recipeId: Long): List<RecipeIngredient>
    
    fun deleteByRecipeId(recipeId: Long)
}

