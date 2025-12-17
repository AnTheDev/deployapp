package com.smartgrocery.repository

import com.smartgrocery.entity.MealItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MealItemRepository : JpaRepository<MealItem, Long> {
    
    @Query("SELECT mi FROM MealItem mi WHERE mi.mealPlan.id = :mealPlanId ORDER BY mi.orderIndex ASC")
    fun findByMealPlanId(mealPlanId: Long): List<MealItem>
    
    fun deleteByMealPlanId(mealPlanId: Long)

    @Query("SELECT MAX(mi.orderIndex) FROM MealItem mi WHERE mi.mealPlan.id = :mealPlanId")
    fun findMaxOrderIndex(mealPlanId: Long): Int?
}

