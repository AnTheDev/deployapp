package com.smartgrocery.entity

import jakarta.persistence.*

@Entity
@Table(name = "meal_items")
class MealItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id", nullable = false)
    var mealPlan: MealPlan,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    var recipe: Recipe? = null,

    @Column(name = "custom_dish_name", length = 200)
    var customDishName: String? = null,

    @Column(name = "servings")
    var servings: Int = 1,

    @Column(name = "order_index", nullable = false)
    var orderIndex: Int = 0,

    @Column(name = "note", length = 255)
    var note: String? = null
) : BaseEntity() {
    fun getDishName(): String {
        return recipe?.title ?: customDishName ?: "Unknown"
    }
}

