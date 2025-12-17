package com.smartgrocery.controller

import com.smartgrocery.dto.category.*
import com.smartgrocery.dto.common.ApiResponse
import com.smartgrocery.service.CategoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "Category management APIs")
class CategoryController(
    private val categoryService: CategoryService
) {

    @GetMapping
    @Operation(summary = "Get all categories")
    fun getAllCategories(): ResponseEntity<ApiResponse<List<CategoryResponse>>> {
        val categories = categoryService.getAllCategories()
        return ResponseEntity.ok(ApiResponse.success(categories))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    fun getCategoryById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<CategoryResponse>> {
        val category = categoryService.getCategoryById(id)
        return ResponseEntity.ok(ApiResponse.success(category))
    }

    @GetMapping("/search")
    @Operation(summary = "Search categories by name")
    fun searchCategories(
        @RequestParam name: String
    ): ResponseEntity<ApiResponse<List<CategoryResponse>>> {
        val categories = categoryService.searchCategories(name)
        return ResponseEntity.ok(ApiResponse.success(categories))
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new category (Admin only)")
    fun createCategory(
        @Valid @RequestBody request: CreateCategoryRequest
    ): ResponseEntity<ApiResponse<CategoryResponse>> {
        val category = categoryService.createCategory(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(category))
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a category (Admin only)")
    fun updateCategory(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateCategoryRequest
    ): ResponseEntity<ApiResponse<CategoryResponse>> {
        val category = categoryService.updateCategory(id, request)
        return ResponseEntity.ok(ApiResponse.success(category, "Category updated successfully"))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a category (Admin only)")
    fun deleteCategory(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<Nothing>> {
        categoryService.deleteCategory(id)
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully"))
    }
}

