package com.smartgrocery.repository

import com.smartgrocery.entity.Recipe
import com.smartgrocery.entity.RecipeDifficulty
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : JpaRepository<Recipe, Long> {
    
    fun findByIsPublicTrue(): List<Recipe>
    
    fun findByIsPublicTrue(pageable: Pageable): Page<Recipe>
    
    fun findByTitleContainingIgnoreCase(title: String): List<Recipe>
    
    fun findByDifficulty(difficulty: RecipeDifficulty): List<Recipe>

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.ingredients WHERE r.id = :id")
    fun findByIdWithIngredients(id: Long): Recipe?

    @Query("""
        SELECT r FROM Recipe r 
        WHERE r.isPublic = true 
        OR r.createdBy.id = :userId
    """)
    fun findAvailableForUser(userId: Long): List<Recipe>

    @Query("""
        SELECT r FROM Recipe r 
        WHERE r.isPublic = true 
        OR r.createdBy.id = :userId
    """)
    fun findAvailableForUser(userId: Long, pageable: Pageable): Page<Recipe>

    @Query("""
        SELECT DISTINCT r FROM Recipe r 
        JOIN r.ingredients ri 
        WHERE ri.masterProduct.id IN :productIds 
        AND r.isPublic = true
    """)
    fun findByIngredientProductIds(productIds: List<Long>): List<Recipe>

    fun findByCreatedById(userId: Long): List<Recipe>
}

