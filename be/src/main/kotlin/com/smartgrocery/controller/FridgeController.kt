package com.smartgrocery.controller

import com.smartgrocery.dto.common.ApiResponse
import com.smartgrocery.dto.common.PageResponse
import com.smartgrocery.dto.fridge.*
import com.smartgrocery.entity.FridgeItemStatus
import com.smartgrocery.entity.FridgeLocation
import com.smartgrocery.service.FridgeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Fridge", description = "Fridge inventory management APIs")
class FridgeController(
    private val fridgeService: FridgeService
) {

    @PostMapping("/fridge-items")
    @Operation(summary = "Add an item to the fridge")
    fun addFridgeItem(
        @Valid @RequestBody request: CreateFridgeItemRequest
    ): ResponseEntity<ApiResponse<FridgeItemResponse>> {
        val item = fridgeService.addFridgeItem(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(item, "Item added to fridge"))
    }

    @GetMapping("/families/{familyId}/fridge-items")
    @Operation(summary = "Get all fridge items for a family with optional filters")
    fun getFridgeItemsByFamily(
        @PathVariable familyId: Long,
        @RequestParam(required = false) location: FridgeLocation?,
        @RequestParam(required = false) status: FridgeItemStatus?,
        @RequestParam(required = false) expiringWithinDays: Int?,
        @PageableDefault(size = 50) pageable: Pageable
    ): ResponseEntity<ApiResponse<PageResponse<FridgeItemResponse>>> {
        val items = fridgeService.getFridgeItemsByFamily(familyId, location, status, expiringWithinDays, pageable)
        return ResponseEntity.ok(ApiResponse.success(items))
    }

    @GetMapping("/families/{familyId}/fridge-items/active")
    @Operation(summary = "Get all active (non-consumed, non-discarded) fridge items")
    fun getActiveFridgeItems(
        @PathVariable familyId: Long
    ): ResponseEntity<ApiResponse<List<FridgeItemResponse>>> {
        val items = fridgeService.getActiveFridgeItems(familyId)
        return ResponseEntity.ok(ApiResponse.success(items))
    }

    @GetMapping("/families/{familyId}/fridge-items/expiring")
    @Operation(summary = "Get items expiring within specified days (default 3)")
    fun getExpiringItems(
        @PathVariable familyId: Long,
        @RequestParam(defaultValue = "3") days: Int
    ): ResponseEntity<ApiResponse<List<FridgeItemResponse>>> {
        val items = fridgeService.getExpiringItems(familyId, days)
        return ResponseEntity.ok(ApiResponse.success(items))
    }

    @GetMapping("/families/{familyId}/fridge-items/expired")
    @Operation(summary = "Get all expired items")
    fun getExpiredItems(
        @PathVariable familyId: Long
    ): ResponseEntity<ApiResponse<List<FridgeItemResponse>>> {
        val items = fridgeService.getExpiredItems(familyId)
        return ResponseEntity.ok(ApiResponse.success(items))
    }

    @GetMapping("/families/{familyId}/fridge-items/statistics")
    @Operation(summary = "Get fridge statistics for a family")
    fun getFridgeStatistics(
        @PathVariable familyId: Long
    ): ResponseEntity<ApiResponse<FridgeStatisticsResponse>> {
        val stats = fridgeService.getFridgeStatistics(familyId)
        return ResponseEntity.ok(ApiResponse.success(stats))
    }

    @GetMapping("/fridge-items/{id}")
    @Operation(summary = "Get fridge item by ID")
    fun getFridgeItemById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<FridgeItemResponse>> {
        val item = fridgeService.getFridgeItemById(id)
        return ResponseEntity.ok(ApiResponse.success(item))
    }

    @PatchMapping("/fridge-items/{id}")
    @Operation(summary = "Update a fridge item")
    fun updateFridgeItem(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateFridgeItemRequest
    ): ResponseEntity<ApiResponse<FridgeItemResponse>> {
        val item = fridgeService.updateFridgeItem(id, request)
        return ResponseEntity.ok(ApiResponse.success(item, "Item updated successfully"))
    }

    @PostMapping("/fridge-items/{id}/consume")
    @Operation(summary = "Consume a quantity of a fridge item")
    fun consumeItem(
        @PathVariable id: Long,
        @Valid @RequestBody request: ConsumeItemRequest
    ): ResponseEntity<ApiResponse<FridgeItemResponse>> {
        val item = fridgeService.consumeItem(id, request)
        return ResponseEntity.ok(ApiResponse.success(item, "Item consumed"))
    }

    @PostMapping("/fridge-items/{id}/discard")
    @Operation(summary = "Mark a fridge item as discarded")
    fun discardItem(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<FridgeItemResponse>> {
        val item = fridgeService.discardItem(id)
        return ResponseEntity.ok(ApiResponse.success(item, "Item discarded"))
    }

    @DeleteMapping("/fridge-items/{id}")
    @Operation(summary = "Delete a fridge item")
    fun deleteFridgeItem(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<Nothing>> {
        fridgeService.deleteFridgeItem(id)
        return ResponseEntity.ok(ApiResponse.success("Item deleted successfully"))
    }
}

