package com.smartgrocery.repository

import com.smartgrocery.entity.ShoppingList
import com.smartgrocery.entity.ShoppingListStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ShoppingListRepository : JpaRepository<ShoppingList, Long> {
    
    fun findByFamilyId(familyId: Long): List<ShoppingList>
    
    fun findByFamilyId(familyId: Long, pageable: Pageable): Page<ShoppingList>
    
    fun findByFamilyIdAndStatus(familyId: Long, status: ShoppingListStatus): List<ShoppingList>

    @Query("SELECT sl FROM ShoppingList sl LEFT JOIN FETCH sl.items WHERE sl.id = :id")
    fun findByIdWithItems(id: Long): ShoppingList?

    @Query("""
        SELECT sl FROM ShoppingList sl 
        WHERE sl.family.id = :familyId 
        AND sl.status IN :statuses 
        ORDER BY sl.createdAt DESC
    """)
    fun findByFamilyIdAndStatusIn(familyId: Long, statuses: List<ShoppingListStatus>): List<ShoppingList>

    @Query("SELECT sl FROM ShoppingList sl WHERE sl.createdBy.id = :userId ORDER BY sl.createdAt DESC")
    fun findByCreatedByUserId(userId: Long): List<ShoppingList>
}

