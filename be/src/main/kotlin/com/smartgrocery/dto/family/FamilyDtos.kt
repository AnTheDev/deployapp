package com.smartgrocery.dto.family

import com.smartgrocery.dto.auth.UserResponse
import com.smartgrocery.entity.FamilyRole
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.Instant

data class CreateFamilyRequest(
    @field:NotBlank(message = "Family name is required")
    @field:Size(max = 100, message = "Family name must not exceed 100 characters")
    val name: String,

    @field:Size(max = 255, message = "Description must not exceed 255 characters")
    val description: String? = null
)

data class UpdateFamilyRequest(
    @field:Size(max = 100, message = "Family name must not exceed 100 characters")
    val name: String? = null,

    @field:Size(max = 255, message = "Description must not exceed 255 characters")
    val description: String? = null
)

data class JoinFamilyRequest(
    @field:NotBlank(message = "Invite code is required")
    val inviteCode: String,

    @field:Size(max = 50, message = "Nickname must not exceed 50 characters")
    val nickname: String? = null
)

data class FamilyResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val inviteCode: String,
    val createdBy: UserSimpleResponse,
    val memberCount: Int,
    val createdAt: Instant
)

data class FamilyDetailResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val inviteCode: String,
    val createdBy: UserSimpleResponse,
    val members: List<FamilyMemberResponse>,
    val createdAt: Instant
)

data class FamilyMemberResponse(
    val userId: Long,
    val username: String,
    val fullName: String,
    val email: String,
    val role: FamilyRole,
    val nickname: String?,
    val joinedAt: Instant
)

data class UserSimpleResponse(
    val id: Long,
    val username: String,
    val fullName: String
)

data class UpdateMemberRequest(
    val nickname: String? = null,
    val role: FamilyRole? = null
)

data class RegenerateInviteCodeResponse(
    val inviteCode: String
)

