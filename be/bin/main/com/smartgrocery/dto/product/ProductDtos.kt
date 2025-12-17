package com.smartgrocery.dto.product

import com.smartgrocery.dto.category.CategoryResponse
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class CreateProductRequest(
    @field:NotBlank(message = "Product name is required")
    @field:Size(max = 200, message = "Product name must not exceed 200 characters")
    val name: String,

    val imageUrl: String? = null,

    @field:NotBlank(message = "Default unit is required")
    @field:Size(max = 50, message = "Default unit must not exceed 50 characters")
    val defaultUnit: String,

    @field:Positive(message = "Average shelf life must be positive")
    val avgShelfLife: Int? = null,

    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,

    val categoryIds: List<Long> = emptyList()
)

data class UpdateProductRequest(
    @field:Size(max = 200, message = "Product name must not exceed 200 characters")
    val name: String? = null,

    val imageUrl: String? = null,

    @field:Size(max = 50, message = "Default unit must not exceed 50 characters")
    val defaultUnit: String? = null,

    @field:Positive(message = "Average shelf life must be positive")
    val avgShelfLife: Int? = null,

    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,

    val categoryIds: List<Long>? = null,

    val isActive: Boolean? = null
)

data class ProductResponse(
    val id: Long,
    val name: String,
    val imageUrl: String?,
    val defaultUnit: String,
    val avgShelfLife: Int?,
    val description: String?,
    val isActive: Boolean,
    val categories: List<CategoryResponse> = emptyList()
)

data class ProductSimpleResponse(
    val id: Long,
    val name: String,
    val imageUrl: String?,
    val defaultUnit: String
)

