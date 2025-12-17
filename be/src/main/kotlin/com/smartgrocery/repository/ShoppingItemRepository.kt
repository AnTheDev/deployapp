package com.smartgrocery.repository

import com.smartgrocery.entity.ShoppingItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ShoppingItemRepository : JpaRepository<ShoppingItem, Long> {
    
    fun findByShoppingListId(listId: Long): List<ShoppingItem>
    
    @Query("SELECT si FROM ShoppingItem si WHERE si.shoppingList.id = :listId AND si.isBought = false")
    fun findUnboughtByListId(listId: Long): List<ShoppingItem>
    
    @Query("SELECT si FROM ShoppingItem si WHERE si.shoppingList.id = :listId AND si.isBought = true")
    fun findBoughtByListId(listId: Long): List<ShoppingItem>
    
    @Query("SELECT si FROM ShoppingItem si WHERE si.assignedTo.id = :userId AND si.isBought = false")
    fun findUnboughtByAssignedTo(userId: Long): List<ShoppingItem>

    @Query("SELECT COUNT(si) FROM ShoppingItem si WHERE si.shoppingList.id = :listId")
    fun countByListId(listId: Long): Long

    @Query("SELECT COUNT(si) FROM ShoppingItem si WHERE si.shoppingList.id = :listId AND si.isBought = true")
    fun countBoughtByListId(listId: Long): Long
}

