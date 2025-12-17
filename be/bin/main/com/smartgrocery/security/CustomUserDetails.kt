package com.smartgrocery.security

import com.smartgrocery.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    private val user: User
) : UserDetails {

    val id: Long get() = user.id!!
    val email: String get() = user.email
    val fullName: String get() = user.fullName

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return user.roles.map { role ->
            SimpleGrantedAuthority("ROLE_${role.name}")
        }
    }

    override fun getPassword(): String = user.passwordHash

    override fun getUsername(): String = user.username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = user.isActive
}

