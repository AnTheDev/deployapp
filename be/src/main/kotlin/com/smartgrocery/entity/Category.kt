package com.smartgrocery.entity

import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false, length = 100)
    var name: String,

    @Column(name = "icon_url", length = 500)
    var iconUrl: String? = null,

    @Column(name = "description", length = 255)
    var description: String? = null,

    @Column(name = "display_order")
    var displayOrder: Int = 0,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true
) : BaseEntity()

