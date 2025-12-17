package com.smartgrocery.repository

import com.smartgrocery.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    
    fun findByIsActiveTrue(): List<Category>
    
    fun findByNameContainingIgnoreCase(name: String): List<Category>

    @Query("SELECT c FROM Category c WHERE c.isActive = true ORDER BY c.displayOrder ASC, c.name ASC")
    fun findAllActiveOrdered(): List<Category>
    
    fun existsByName(name: String): Boolean
}

