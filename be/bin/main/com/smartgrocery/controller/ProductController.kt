package com.smartgrocery.controller

import com.smartgrocery.dto.common.ApiResponse
import com.smartgrocery.dto.common.PageResponse
import com.smartgrocery.dto.product.*
import com.smartgrocery.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/master-products")
@Tag(name = "Master Products", description = "Product catalog management APIs")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    @Operation(summary = "Get all products with pagination")
    fun getAllProducts(
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> {
        val products = productService.getAllProducts(pageable)
        return ResponseEntity.ok(ApiResponse.success(products))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    fun getProductById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<ProductResponse>> {
        val product = productService.getProductById(id)
        return ResponseEntity.ok(ApiResponse.success(product))
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by name")
    fun searchProducts(
        @RequestParam name: String,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> {
        val products = productService.searchProducts(name, pageable)
        return ResponseEntity.ok(ApiResponse.success(products))
    }

    @GetMapping("/by-category/{categoryId}")
    @Operation(summary = "Get products by category")
    fun getProductsByCategory(
        @PathVariable categoryId: Long,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> {
        val products = productService.getProductsByCategory(categoryId, pageable)
        return ResponseEntity.ok(ApiResponse.success(products))
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new product (Admin only)")
    fun createProduct(
        @Valid @RequestBody request: CreateProductRequest
    ): ResponseEntity<ApiResponse<ProductResponse>> {
        val product = productService.createProduct(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(product))
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a product (Admin only)")
    fun updateProduct(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateProductRequest
    ): ResponseEntity<ApiResponse<ProductResponse>> {
        val product = productService.updateProduct(id, request)
        return ResponseEntity.ok(ApiResponse.success(product, "Product updated successfully"))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a product (Admin only)")
    fun deleteProduct(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<Nothing>> {
        productService.deleteProduct(id)
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully"))
    }
}

