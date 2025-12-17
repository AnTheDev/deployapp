package com.smartgrocery.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "recipe_ingredients")
class RecipeIngredient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    var recipe: Recipe,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_product_id")
    var masterProduct: MasterProduct? = null,

    @Column(name = "custom_ingredient_name", length = 200)
    var customIngredientName: String? = null,

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    var quantity: BigDecimal = BigDecimal.ONE,

    @Column(name = "unit", nullable = false, length = 50)
    var unit: String,

    @Column(name = "note", length = 255)
    var note: String? = null,

    @Column(name = "is_optional", nullable = false)
    var isOptional: Boolean = false
) {
    fun getIngredientName(): String {
        return masterProduct?.name ?: customIngredientName ?: "Unknown"
    }
}

