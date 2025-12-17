package com.smartgrocery.controller

import com.smartgrocery.dto.common.ApiResponse
import com.smartgrocery.dto.common.PageResponse
import com.smartgrocery.dto.shopping.*
import com.smartgrocery.service.ShoppingListService
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
@Tag(name = "Shopping Lists", description = "Shopping list management APIs")
class ShoppingListController(
    private val shoppingListService: ShoppingListService
) {

    @PostMapping("/shopping-lists")
    @Operation(summary = "Create a new shopping list")
    fun createShoppingList(
        @Valid @RequestBody request: CreateShoppingListRequest
    ): ResponseEntity<ApiResponse<ShoppingListDetailResponse>> {
        val list = shoppingListService.createShoppingList(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(list, "Shopping list created successfully"))
    }

    @GetMapping("/families/{familyId}/shopping-lists")
    @Operation(summary = "Get all shopping lists for a family")
    fun getShoppingListsByFamily(
        @PathVariable familyId: Long,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<ApiResponse<PageResponse<ShoppingListResponse>>> {
        val lists = shoppingListService.getShoppingListsByFamily(familyId, pageable)
        return ResponseEntity.ok(ApiResponse.success(lists))
    }

    @GetMapping("/families/{familyId}/shopping-lists/active")
    @Operation(summary = "Get active shopping lists for a family")
    fun getActiveShoppingLists(
        @PathVariable familyId: Long
    ): ResponseEntity<ApiResponse<List<ShoppingListResponse>>> {
        val lists = shoppingListService.getActiveListsByFamily(familyId)
        return ResponseEntity.ok(ApiResponse.success(lists))
    }

    @GetMapping("/shopping-lists/{id}")
    @Operation(summary = "Get shopping list by ID with all items")
    fun getShoppingListById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<ShoppingListDetailResponse>> {
        val list = shoppingListService.getShoppingListById(id)
        return ResponseEntity.ok(ApiResponse.success(list))
    }

    @PatchMapping("/shopping-lists/{id}")
    @Operation(summary = "Update a shopping list (handles optimistic locking)")
    fun updateShoppingList(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateShoppingListRequest
    ): ResponseEntity<ApiResponse<ShoppingListDetailResponse>> {
        val list = shoppingListService.updateShoppingList(id, request)
        return ResponseEntity.ok(ApiResponse.success(list, "Shopping list updated successfully"))
    }

    @DeleteMapping("/shopping-lists/{id}")
    @Operation(summary = "Delete a shopping list")
    fun deleteShoppingList(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<Nothing>> {
        shoppingListService.deleteShoppingList(id)
        return ResponseEntity.ok(ApiResponse.success("Shopping list deleted successfully"))
    }

    @PostMapping("/shopping-lists/{listId}/items")
    @Operation(summary = "Add an item to a shopping list")
    fun addItemToList(
        @PathVariable listId: Long,
        @Valid @RequestBody request: CreateShoppingItemRequest
    ): ResponseEntity<ApiResponse<ShoppingItemResponse>> {
        val addRequest = AddItemToListRequest(listId = listId, item = request)
        val item = shoppingListService.addItemToList(addRequest)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(item, "Item added successfully"))
    }

    @PostMapping("/shopping-lists/{listId}/items/bulk")
    @Operation(summary = "Add multiple items to a shopping list")
    fun addItemsToList(
        @PathVariable listId: Long,
        @Valid @RequestBody items: List<CreateShoppingItemRequest>
    ): ResponseEntity<ApiResponse<List<ShoppingItemResponse>>> {
        val request = BulkAddItemsRequest(listId = listId, items = items)
        val addedItems = shoppingListService.addItemsToList(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(addedItems, "Items added successfully"))
    }

    @PatchMapping("/shopping-items/{itemId}")
    @Operation(summary = "Update a shopping item (handles optimistic locking)")
    fun updateShoppingItem(
        @PathVariable itemId: Long,
        @Valid @RequestBody request: UpdateShoppingItemRequest
    ): ResponseEntity<ApiResponse<ShoppingItemResponse>> {
        val item = shoppingListService.updateShoppingItem(itemId, request)
        return ResponseEntity.ok(ApiResponse.success(item, "Item updated successfully"))
    }

    @DeleteMapping("/shopping-items/{itemId}")
    @Operation(summary = "Delete a shopping item")
    fun deleteShoppingItem(
        @PathVariable itemId: Long
    ): ResponseEntity<ApiResponse<Nothing>> {
        shoppingListService.deleteShoppingItem(itemId)
        return ResponseEntity.ok(ApiResponse.success("Item deleted successfully"))
    }
}

