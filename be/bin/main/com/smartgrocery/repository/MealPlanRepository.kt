package com.smartgrocery.repository

import com.smartgrocery.entity.MealPlan
import com.smartgrocery.entity.MealType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface MealPlanRepository : JpaRepository<MealPlan, Long> {
    
    fun findByFamilyIdAndDate(familyId: Long, date: LocalDate): List<MealPlan>
    
    fun findByFamilyIdAndDateAndMealType(familyId: Long, date: LocalDate, mealType: MealType): MealPlan?

    @Query("SELECT mp FROM MealPlan mp LEFT JOIN FETCH mp.items WHERE mp.id = :id")
    fun findByIdWithItems(id: Long): MealPlan?

    @Query("""
        SELECT mp FROM MealPlan mp 
        WHERE mp.family.id = :familyId 
        AND mp.date BETWEEN :startDate AND :endDate 
        ORDER BY mp.date ASC, mp.mealType ASC
    """)
    fun findByFamilyIdAndDateBetween(familyId: Long, startDate: LocalDate, endDate: LocalDate): List<MealPlan>

    @Query("""
        SELECT DISTINCT mp FROM MealPlan mp 
        LEFT JOIN FETCH mp.items 
        WHERE mp.family.id = :familyId 
        AND mp.date BETWEEN :startDate AND :endDate 
        ORDER BY mp.date ASC
    """)
    fun findByFamilyIdAndDateBetweenWithItems(familyId: Long, startDate: LocalDate, endDate: LocalDate): List<MealPlan>

    fun existsByFamilyIdAndDateAndMealType(familyId: Long, date: LocalDate, mealType: MealType): Boolean
}

