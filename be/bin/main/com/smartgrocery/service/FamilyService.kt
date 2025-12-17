package com.smartgrocery.service

import com.smartgrocery.dto.family.*
import com.smartgrocery.entity.Family
import com.smartgrocery.entity.FamilyMember
import com.smartgrocery.entity.FamilyMemberId
import com.smartgrocery.entity.FamilyRole
import com.smartgrocery.exception.*
import com.smartgrocery.repository.FamilyMemberRepository
import com.smartgrocery.repository.FamilyRepository
import com.smartgrocery.repository.UserRepository
import com.smartgrocery.security.CustomUserDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class FamilyService(
    private val familyRepository: FamilyRepository,
    private val familyMemberRepository: FamilyMemberRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun createFamily(request: CreateFamilyRequest): FamilyResponse {
        val currentUser = getCurrentUser()
        val user = userRepository.findById(currentUser.id)
            .orElseThrow { ResourceNotFoundException(ErrorCode.USER_NOT_FOUND) }

        val inviteCode = generateInviteCode()

        val family = Family(
            name = request.name,
            description = request.description,
            inviteCode = inviteCode,
            createdBy = user
        )

        val savedFamily = familyRepository.save(family)

        // Add creator as LEADER
        val member = FamilyMember(
            id = FamilyMemberId(savedFamily.id!!, user.id!!),
            family = savedFamily,
            user = user,
            role = FamilyRole.LEADER,
            joinedAt = Instant.now()
        )
        familyMemberRepository.save(member)

        return toFamilyResponse(savedFamily, 1)
    }

    @Transactional
    fun joinFamily(request: JoinFamilyRequest): FamilyResponse {
        val currentUser = getCurrentUser()
        val user = userRepository.findById(currentUser.id)
            .orElseThrow { ResourceNotFoundException(ErrorCode.USER_NOT_FOUND) }

        val family = familyRepository.findByInviteCode(request.inviteCode)
            ?: throw ApiException(ErrorCode.INVALID_INVITE_CODE)

        // Check if already a member
        if (familyMemberRepository.existsByFamilyIdAndUserId(family.id!!, user.id!!)) {
            throw ConflictException(ErrorCode.ALREADY_MEMBER)
        }

        val member = FamilyMember(
            id = FamilyMemberId(family.id!!, user.id!!),
            family = family,
            user = user,
            role = FamilyRole.MEMBER,
            nickname = request.nickname,
            joinedAt = Instant.now()
        )
        familyMemberRepository.save(member)

        val memberCount = familyMemberRepository.findByFamilyIdWithUsers(family.id!!).size
        return toFamilyResponse(family, memberCount)
    }

    fun getFamilyById(familyId: Long): FamilyDetailResponse {
        val currentUser = getCurrentUser()
        
        // Check membership
        if (!familyMemberRepository.existsByFamilyIdAndUserId(familyId, currentUser.id)) {
            throw ForbiddenException("You are not a member of this family")
        }

        val family = familyRepository.findByIdWithMembers(familyId)
            ?: throw ResourceNotFoundException(ErrorCode.FAMILY_NOT_FOUND)

        return toFamilyDetailResponse(family)
    }

    fun getMyFamilies(): List<FamilyResponse> {
        val currentUser = getCurrentUser()
        val memberships = familyMemberRepository.findByUserIdWithFamily(currentUser.id)

        return memberships.map { membership ->
            val memberCount = familyMemberRepository.findByFamilyIdWithUsers(membership.family.id!!).size
            toFamilyResponse(membership.family, memberCount)
        }
    }

    fun getFamilyMembers(familyId: Long): List<FamilyMemberResponse> {
        val currentUser = getCurrentUser()
        
        // Check membership
        if (!familyMemberRepository.existsByFamilyIdAndUserId(familyId, currentUser.id)) {
            throw ForbiddenException("You are not a member of this family")
        }

        val members = familyMemberRepository.findByFamilyIdWithUsers(familyId)
        return members.map { toFamilyMemberResponse(it) }
    }

    @Transactional
    fun updateFamily(familyId: Long, request: UpdateFamilyRequest): FamilyResponse {
        val currentUser = getCurrentUser()
        checkLeaderPermission(familyId, currentUser.id)

        val family = familyRepository.findById(familyId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.FAMILY_NOT_FOUND) }

        request.name?.let { family.name = it }
        request.description?.let { family.description = it }

        val savedFamily = familyRepository.save(family)
        val memberCount = familyMemberRepository.findByFamilyIdWithUsers(familyId).size

        return toFamilyResponse(savedFamily, memberCount)
    }

    @Transactional
    fun updateMember(familyId: Long, userId: Long, request: UpdateMemberRequest): FamilyMemberResponse {
        val currentUser = getCurrentUser()
        checkLeaderPermission(familyId, currentUser.id)

        val member = familyMemberRepository.findByFamilyIdAndUserId(familyId, userId)
            ?: throw ResourceNotFoundException(ErrorCode.NOT_A_MEMBER)

        request.nickname?.let { member.nickname = it }
        request.role?.let { member.role = it }

        val savedMember = familyMemberRepository.save(member)
        return toFamilyMemberResponse(savedMember)
    }

    @Transactional
    fun removeMember(familyId: Long, userId: Long) {
        val currentUser = getCurrentUser()
        checkLeaderPermission(familyId, currentUser.id)

        val member = familyMemberRepository.findByFamilyIdAndUserId(familyId, userId)
            ?: throw ResourceNotFoundException(ErrorCode.NOT_A_MEMBER)

        if (member.role == FamilyRole.LEADER) {
            throw ApiException(ErrorCode.CANNOT_REMOVE_LEADER)
        }

        familyMemberRepository.delete(member)
    }

    @Transactional
    fun leaveFamily(familyId: Long) {
        val currentUser = getCurrentUser()
        
        val member = familyMemberRepository.findByFamilyIdAndUserId(familyId, currentUser.id)
            ?: throw ResourceNotFoundException(ErrorCode.NOT_A_MEMBER)

        if (member.role == FamilyRole.LEADER) {
            // Check if there are other members
            val members = familyMemberRepository.findByFamilyIdWithUsers(familyId)
            if (members.size > 1) {
                throw ApiException(ErrorCode.CANNOT_REMOVE_LEADER, 
                    "Please transfer leadership before leaving the family")
            }
            // If leader is the only member, delete the family
            familyRepository.deleteById(familyId)
        } else {
            familyMemberRepository.delete(member)
        }
    }

    @Transactional
    fun regenerateInviteCode(familyId: Long): RegenerateInviteCodeResponse {
        val currentUser = getCurrentUser()
        checkLeaderPermission(familyId, currentUser.id)

        val family = familyRepository.findById(familyId)
            .orElseThrow { ResourceNotFoundException(ErrorCode.FAMILY_NOT_FOUND) }

        family.inviteCode = generateInviteCode()
        familyRepository.save(family)

        return RegenerateInviteCodeResponse(family.inviteCode)
    }

    @Transactional
    fun deleteFamily(familyId: Long) {
        val currentUser = getCurrentUser()
        checkLeaderPermission(familyId, currentUser.id)

        familyRepository.deleteById(familyId)
    }

    private fun generateInviteCode(): String {
        var code: String
        do {
            code = UUID.randomUUID().toString().substring(0, 8).uppercase()
        } while (familyRepository.existsByInviteCode(code))
        return code
    }

    private fun checkLeaderPermission(familyId: Long, userId: Long) {
        val member = familyMemberRepository.findByFamilyIdAndUserId(familyId, userId)
            ?: throw ForbiddenException("You are not a member of this family")

        if (member.role != FamilyRole.LEADER) {
            throw ForbiddenException(ErrorCode.NOT_FAMILY_LEADER.message)
        }
    }

    fun isFamilyMember(familyId: Long, userId: Long): Boolean {
        return familyMemberRepository.existsByFamilyIdAndUserId(familyId, userId)
    }

    private fun getCurrentUser(): CustomUserDetails {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal as CustomUserDetails
    }

    private fun toFamilyResponse(family: Family, memberCount: Int): FamilyResponse {
        return FamilyResponse(
            id = family.id!!,
            name = family.name,
            description = family.description,
            inviteCode = family.inviteCode,
            createdBy = UserSimpleResponse(
                id = family.createdBy.id!!,
                username = family.createdBy.username,
                fullName = family.createdBy.fullName
            ),
            memberCount = memberCount,
            createdAt = family.createdAt
        )
    }

    private fun toFamilyDetailResponse(family: Family): FamilyDetailResponse {
        return FamilyDetailResponse(
            id = family.id!!,
            name = family.name,
            description = family.description,
            inviteCode = family.inviteCode,
            createdBy = UserSimpleResponse(
                id = family.createdBy.id!!,
                username = family.createdBy.username,
                fullName = family.createdBy.fullName
            ),
            members = family.members.map { toFamilyMemberResponse(it) },
            createdAt = family.createdAt
        )
    }

    private fun toFamilyMemberResponse(member: FamilyMember): FamilyMemberResponse {
        return FamilyMemberResponse(
            userId = member.user.id!!,
            username = member.user.username,
            fullName = member.user.fullName,
            email = member.user.email,
            role = member.role,
            nickname = member.nickname,
            joinedAt = member.joinedAt
        )
    }
}

