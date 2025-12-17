package com.smartgrocery.entity

import jakarta.persistence.*

@Entity
@Table(name = "families")
class Family(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false, length = 100)
    var name: String,

    @Column(name = "invite_code", nullable = false, unique = true, length = 10)
    var inviteCode: String,

    @Column(name = "description", length = 255)
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: User,

    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var members: MutableSet<FamilyMember> = mutableSetOf()
) : BaseEntity()

