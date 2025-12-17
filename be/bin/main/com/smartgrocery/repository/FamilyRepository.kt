package com.smartgrocery.repository

import com.smartgrocery.entity.Family
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FamilyRepository : JpaRepository<Family, Long> {
    
    fun findByInviteCode(inviteCode: String): Family?
    
    fun existsByInviteCode(inviteCode: String): Boolean

    @Query("SELECT f FROM Family f LEFT JOIN FETCH f.members WHERE f.id = :id")
    fun findByIdWithMembers(id: Long): Family?

    @Query("""
        SELECT f FROM Family f 
        JOIN f.members m 
        WHERE m.user.id = :userId
    """)
    fun findAllByUserId(userId: Long): List<Family>
}

