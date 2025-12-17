package com.smartgrocery.repository

import com.smartgrocery.entity.MasterProduct
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MasterProductRepository : JpaRepository<MasterProduct, Long> {
    
    fun findByIsActiveTrue(): List<MasterProduct>
    
    fun findByIsActiveTrue(pageable: Pageable): Page<MasterProduct>
    
    fun findByNameContainingIgnoreCaseAndIsActiveTrue(name: String): List<MasterProduct>
    
    fun findByNameContainingIgnoreCaseAndIsActiveTrue(name: String, pageable: Pageable): Page<MasterProduct>

    @Query("SELECT p FROM MasterProduct p LEFT JOIN FETCH p.categories WHERE p.id = :id")
    fun findByIdWithCategories(id: Long): MasterProduct?

    @Query("""
        SELECT DISTINCT p FROM MasterProduct p 
        JOIN p.categories c 
        WHERE c.id = :categoryId AND p.isActive = true
    """)
    fun findByCategoryId(categoryId: Long): List<MasterProduct>

    @Query("""
        SELECT DISTINCT p FROM MasterProduct p 
        JOIN p.categories c 
        WHERE c.id = :categoryId AND p.isActive = true
    """)
    fun findByCategoryId(categoryId: Long, pageable: Pageable): Page<MasterProduct>

    fun existsByName(name: String): Boolean
}

