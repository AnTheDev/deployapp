package com.smartgrocery.entity

import jakarta.persistence.*
import java.io.Serializable
import java.time.Instant

enum class FamilyRole {
    LEADER,
    MEMBER
}

@Embeddable
class FamilyMemberId(
    @Column(name = "family_id")
    var familyId: Long = 0,

    @Column(name = "user_id")
    var userId: Long = 0
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FamilyMemberId) return false
        return familyId == other.familyId && userId == other.userId
    }

    override fun hashCode(): Int {
        return 31 * familyId.hashCode() + userId.hashCode()
    }
}

@Entity
@Table(name = "family_members")
class FamilyMember(
    @EmbeddedId
    var id: FamilyMemberId = FamilyMemberId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("familyId")
    @JoinColumn(name = "family_id")
    var family: Family,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    var user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    var role: FamilyRole = FamilyRole.MEMBER,

    @Column(name = "nickname", length = 50)
    var nickname: String? = null,

    @Column(name = "joined_at", nullable = false)
    var joinedAt: Instant = Instant.now()
)

