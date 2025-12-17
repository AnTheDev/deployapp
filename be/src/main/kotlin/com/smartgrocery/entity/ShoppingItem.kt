package com.smartgrocery.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "shopping_items")
class ShoppingItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    var shoppingList: ShoppingList,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_product_id")
    var masterProduct: MasterProduct? = null,

    @Column(name = "custom_product_name", length = 200)
    var customProductName: String? = null,

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    var quantity: BigDecimal = BigDecimal.ONE,

    @Column(name = "unit", nullable = false, length = 50)
    var unit: String,

    @Column(name = "is_bought", nullable = false)
    var isBought: Boolean = false,

    @Column(name = "note", length = 255)
    var note: String? = null,

    @Column(name = "price", precision = 12, scale = 2)
    var price: BigDecimal? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    var assignedTo: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bought_by")
    var boughtBy: User? = null,

    @Version
    @Column(name = "version")
    var version: Long = 0
) : BaseEntity() {

    fun getProductName(): String {
        return masterProduct?.name ?: customProductName ?: "Unknown"
    }
}

