package com.smartgrocery.repository

import com.smartgrocery.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    
    fun findByUsername(username: String): User?
    
    fun findByEmail(email: String): User?
    
    fun existsByUsername(username: String): Boolean
    
    fun existsByEmail(email: String): Boolean

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    fun findByUsernameWithRoles(username: String): User?

    @Query("SELECT u FROM User u WHERE u.fcmToken IS NOT NULL AND u.isActive = true")
    fun findAllWithFcmToken(): List<User>
}

