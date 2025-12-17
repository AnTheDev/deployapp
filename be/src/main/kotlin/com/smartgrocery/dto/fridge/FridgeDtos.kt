package com.smartgrocery.dto.fridge

import com.smartgrocery.dto.family.UserSimpleResponse
import com.smartgrocery.dto.product.ProductSimpleResponse
import com.smartgrocery.entity.FridgeItemStatus
import com.smartgrocery.entity.FridgeLocation
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

data class CreateFridgeItemRequest(
    @field:NotNull(message = "Family ID is required")
    val familyId: Long,

    val masterProductId: Long? = null,

    @field:Size(max = 200, message = "Custom product name must not exceed 200 characters")
    val customProductName: String? = null,

    @field:NotNull(message = "Quantity is required")
    @field:Positive(message = "Quantity must be positive")
    val quantity: BigDecimal,

    @field:NotNull(message = "Unit is required")
    @field:Size(max = 50, message = "Unit must not exceed 50 characters")
    val unit: String,

    val expirationDate: LocalDate? = null,

    val location: FridgeLocation = FridgeLocation.COOLER,

    @field:Size(max = 255, message = "Note must not exceed 255 characters")
    val note: String? = null
)

data class UpdateFridgeItemRequest(
    @field:Positive(message = "Quantity must be positive")
    val quantity: BigDecimal? = null,

    @field:Size(max = 50, message = "Unit must not exceed 50 characters")
    val unit: String? = null,

    val expirationDate: LocalDate? = null,

    val location: FridgeLocation? = null,

    val status: FridgeItemStatus? = null,

    @field:Size(max = 255, message = "Note must not exceed 255 characters")
    val note: String? = null
)

data class FridgeItemResponse(
    val id: Long,
    val familyId: Long,
    val productName: String,
    val masterProduct: ProductSimpleResponse?,
    val customProductName: String?,
    val quantity: BigDecimal,
    val unit: String,
    val expirationDate: LocalDate?,
    val location: FridgeLocation,
    val status: FridgeItemStatus,
    val note: String?,
    val addedBy: UserSimpleResponse,
    val isExpiringSoon: Boolean,
    val isExpired: Boolean,
    val daysUntilExpiration: Long?,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class ConsumeItemRequest(
    @field:NotNull(message = "Quantity is required")
    @field:Positive(message = "Quantity must be positive")
    val quantity: BigDecimal
)

data class FridgeStatisticsResponse(
    val totalItems: Int,
    val expiringSoonCount: Int,
    val expiredCount: Int,
    val itemsByLocation: Map<FridgeLocation, Int>,
    val itemsByStatus: Map<FridgeItemStatus, Int>
)

data class ExpiringItemNotification(
    val itemId: Long,
    val productName: String,
    val expirationDate: LocalDate,
    val daysUntilExpiration: Long,
    val familyId: Long,
    val familyName: String
)

