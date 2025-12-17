package com.smartgrocery.repository

import com.smartgrocery.entity.FridgeItem
import com.smartgrocery.entity.FridgeItemStatus
import com.smartgrocery.entity.FridgeLocation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface FridgeItemRepository : JpaRepository<FridgeItem, Long> {
    
    fun findByFamilyId(familyId: Long): List<FridgeItem>
    
    fun findByFamilyId(familyId: Long, pageable: Pageable): Page<FridgeItem>
    
    fun findByFamilyIdAndLocation(familyId: Long, location: FridgeLocation): List<FridgeItem>
    
    fun findByFamilyIdAndStatus(familyId: Long, status: FridgeItemStatus): List<FridgeItem>

    @Query("""
        SELECT fi FROM FridgeItem fi 
        WHERE fi.family.id = :familyId 
        AND fi.status NOT IN ('CONSUMED', 'DISCARDED')
        ORDER BY fi.expirationDate ASC NULLS LAST
    """)
    fun findActiveByFamilyId(familyId: Long): List<FridgeItem>

    @Query("""
        SELECT fi FROM FridgeItem fi 
        WHERE fi.expirationDate IS NOT NULL 
        AND fi.expirationDate <= :thresholdDate 
        AND fi.expirationDate >= :today
        AND fi.status NOT IN ('CONSUMED', 'DISCARDED', 'EXPIRED')
    """)
    fun findExpiringSoon(today: LocalDate, thresholdDate: LocalDate): List<FridgeItem>

    @Query("""
        SELECT fi FROM FridgeItem fi 
        WHERE fi.expirationDate IS NOT NULL 
        AND fi.expirationDate < :today
        AND fi.status NOT IN ('CONSUMED', 'DISCARDED', 'EXPIRED')
    """)
    fun findExpired(today: LocalDate): List<FridgeItem>

    @Query("""
        SELECT fi FROM FridgeItem fi 
        WHERE fi.family.id = :familyId 
        AND fi.masterProduct.id = :productId 
        AND fi.status NOT IN ('CONSUMED', 'DISCARDED')
    """)
    fun findByFamilyIdAndProductId(familyId: Long, productId: Long): List<FridgeItem>

    @Query("""
        SELECT fi FROM FridgeItem fi 
        WHERE fi.family.id = :familyId 
        AND (:location IS NULL OR fi.location = :location)
        AND (:status IS NULL OR fi.status = :status)
        AND (:thresholdDate IS NULL OR (fi.expirationDate IS NOT NULL AND fi.expirationDate <= :thresholdDate AND fi.expirationDate >= :today))
        ORDER BY fi.expirationDate ASC NULLS LAST
    """)
    fun findByFamilyIdWithFilters(
        familyId: Long,
        location: FridgeLocation?,
        status: FridgeItemStatus?,
        today: LocalDate?,
        thresholdDate: LocalDate?,
        pageable: Pageable
    ): Page<FridgeItem>
}

