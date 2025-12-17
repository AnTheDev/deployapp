package com.smartgrocery.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

enum class FridgeLocation {
    FREEZER,
    COOLER,
    PANTRY
}

enum class FridgeItemStatus {
    FRESH,
    EXPIRING_SOON,
    EXPIRED,
    CONSUMED,
    DISCARDED
}

@Entity
@Table(name = "fridge_items")
class FridgeItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id", nullable = false)
    var family: Family,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_product_id")
    var masterProduct: MasterProduct? = null,

    @Column(name = "custom_product_name", length = 200)
    var customProductName: String? = null,

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    var quantity: BigDecimal = BigDecimal.ONE,

    @Column(name = "unit", nullable = false, length = 50)
    var unit: String,

    @Column(name = "expiration_date")
    var expirationDate: LocalDate? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "location", nullable = false, length = 20)
    var location: FridgeLocation = FridgeLocation.COOLER,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    var status: FridgeItemStatus = FridgeItemStatus.FRESH,

    @Column(name = "note", length = 255)
    var note: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by", nullable = false)
    var addedBy: User
) : BaseEntity() {

    fun getProductName(): String {
        return masterProduct?.name ?: customProductName ?: "Unknown"
    }

    fun isExpiringSoon(daysThreshold: Int = 3): Boolean {
        val expDate = expirationDate ?: return false
        val thresholdDate = LocalDate.now().plusDays(daysThreshold.toLong())
        return expDate <= thresholdDate && expDate >= LocalDate.now()
    }

    fun isExpired(): Boolean {
        val expDate = expirationDate ?: return false
        return expDate < LocalDate.now()
    }
}

