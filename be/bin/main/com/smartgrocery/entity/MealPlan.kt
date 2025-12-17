package com.smartgrocery.entity

import jakarta.persistence.*
import java.time.LocalDate

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
}

@Entity
@Table(
    name = "meal_plans",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["family_id", "date", "meal_type"])
    ]
)
class MealPlan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    var family: Family,

    @Column(name = "date", nullable = false)
    var date: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false, length = 20)
    var mealType: MealType,

    @Column(name = "note", length = 500)
    var note: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    var createdBy: User,

    @OneToMany(mappedBy = "mealPlan", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    var items: MutableList<MealItem> = mutableListOf()
) : BaseEntity()

