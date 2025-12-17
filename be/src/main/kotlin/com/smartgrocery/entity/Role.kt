package com.smartgrocery.entity

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false, unique = true, length = 50)
    var name: String,

    @Column(name = "description", length = 255)
    var description: String? = null
)

