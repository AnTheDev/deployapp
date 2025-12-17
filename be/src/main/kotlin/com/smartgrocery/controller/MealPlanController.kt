package com.smartgrocery.controller

import com.smartgrocery.dto.common.ApiResponse
import com.smartgrocery.dto.mealplan.*
import com.smartgrocery.service.MealPlanService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Meal Plans", description = "Meal planning management APIs")
class MealPlanController(
    private val mealPlanService: MealPlanService
) {

    @PostMapping("/meal-plans")
    @Operation(summary = "Create a new meal plan")
    fun createMealPlan(
        @Valid @RequestBody request: CreateMealPlanRequest
    ): ResponseEntity<ApiResponse<MealPlanDetailResponse>> {
        val mealPlan = mealPlanService.createMealPlan(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(mealPlan, "Meal plan created successfully"))
    }

    @GetMapping("/meal-plans/{id}")
    @Operation(summary = "Get meal plan by ID with items")
    fun getMealPlanById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<MealPlanDetailResponse>> {
        val mealPlan = mealPlanService.getMealPlanById(id)
        return ResponseEntity.ok(ApiResponse.success(mealPlan))
    }

    @GetMapping("/families/{familyId}/meal-plans")
    @Operation(summary = "Get meal plans by date range")
    fun getMealPlansByDateRange(
        @PathVariable familyId: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate
    ): ResponseEntity<ApiResponse<List<MealPlanDetailResponse>>> {
        val mealPlans = mealPlanService.getMealPlansByDateRange(familyId, startDate, endDate)
        return ResponseEntity.ok(ApiResponse.success(mealPlans))
    }

    @GetMapping("/families/{familyId}/meal-plans/daily")
    @Operation(summary = "Get daily meal plan (all meal types for a day)")
    fun getDailyMealPlan(
        @PathVariable familyId: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<ApiResponse<DailyMealPlanResponse>> {
        val dailyPlan = mealPlanService.getDailyMealPlan(familyId, date)
        return ResponseEntity.ok(ApiResponse.success(dailyPlan))
    }

    @GetMapping("/families/{familyId}/meal-plans/weekly")
    @Operation(summary = "Get weekly meal plan (7 days starting from date)")
    fun getWeeklyMealPlan(
        @PathVariable familyId: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate
    ): ResponseEntity<ApiResponse<WeeklyMealPlanResponse>> {
        val weeklyPlan = mealPlanService.getWeeklyMealPlan(familyId, startDate)
        return ResponseEntity.ok(ApiResponse.success(weeklyPlan))
    }

    @PutMapping("/meal-plans/{id}")
    @Operation(summary = "Update a meal plan")
    fun updateMealPlan(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateMealPlanRequest
    ): ResponseEntity<ApiResponse<MealPlanDetailResponse>> {
        val mealPlan = mealPlanService.updateMealPlan(id, request)
        return ResponseEntity.ok(ApiResponse.success(mealPlan, "Meal plan updated successfully"))
    }

    @PostMapping("/meal-plans/{mealPlanId}/items")
    @Operation(summary = "Add an item to a meal plan")
    fun addMealItem(
        @PathVariable mealPlanId: Long,
        @Valid @RequestBody request: CreateMealItemRequest
    ): ResponseEntity<ApiResponse<MealItemResponse>> {
        val addRequest = AddMealItemRequest(mealPlanId = mealPlanId, item = request)
        val item = mealPlanService.addMealItem(addRequest)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(item, "Item added to meal plan"))
    }

    @DeleteMapping("/meal-items/{itemId}")
    @Operation(summary = "Delete a meal item")
    fun deleteMealItem(
        @PathVariable itemId: Long
    ): ResponseEntity<ApiResponse<Nothing>> {
        mealPlanService.deleteMealItem(itemId)
        return ResponseEntity.ok(ApiResponse.success("Item deleted successfully"))
    }

    @DeleteMapping("/meal-plans/{id}")
    @Operation(summary = "Delete a meal plan")
    fun deleteMealPlan(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<Nothing>> {
        mealPlanService.deleteMealPlan(id)
        return ResponseEntity.ok(ApiResponse.success("Meal plan deleted successfully"))
    }
}

