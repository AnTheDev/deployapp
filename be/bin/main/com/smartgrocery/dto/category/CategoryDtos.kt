package com.smartgrocery.dto.category

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateCategoryRequest(
    @field:NotBlank(message = "Category name is required")
    @field:Size(max = 100, message = "Category name must not exceed 100 characters")
    val name: String,

    val iconUrl: String? = null,

    @field:Size(max = 255, message = "Description must not exceed 255 characters")
    val description: String? = null,

    val displayOrder: Int = 0
)

data class UpdateCategoryRequest(
    @field:Size(max = 100, message = "Category name must not exceed 100 characters")
    val name: String? = null,

    val iconUrl: String? = null,

    @field:Size(max = 255, message = "Description must not exceed 255 characters")
    val description: String? = null,

    val displayOrder: Int? = null,

    val isActive: Boolean? = null
)

data class CategoryResponse(
    val id: Long,
    val name: String,
    val iconUrl: String?,
    val description: String?,
    val displayOrder: Int,
    val isActive: Boolean
)

