package com.smartgrocery.dto.mealplan

import com.smartgrocery.dto.family.UserSimpleResponse
import com.smartgrocery.dto.recipe.RecipeResponse
import com.smartgrocery.entity.MealType
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDate

data class CreateMealPlanRequest(
    @field:NotNull(message = "Family ID is required")
    val familyId: Long,

    @field:NotNull(message = "Date is required")
    val date: LocalDate,

    @field:NotNull(message = "Meal type is required")
    val mealType: MealType,

    @field:Size(max = 500, message = "Note must not exceed 500 characters")
    val note: String? = null,

    @field:Valid
    val items: List<CreateMealItemRequest> = emptyList()
)

data class UpdateMealPlanRequest(
    @field:Size(max = 500, message = "Note must not exceed 500 characters")
    val note: String? = null,

    @field:Valid
    val items: List<CreateMealItemRequest>? = null
)

data class CreateMealItemRequest(
    val recipeId: Long? = null,

    @field:Size(max = 200, message = "Custom dish name must not exceed 200 characters")
    val customDishName: String? = null,

    @field:Positive(message = "Servings must be positive")
    val servings: Int = 1,

    val orderIndex: Int = 0,

    @field:Size(max = 255, message = "Note must not exceed 255 characters")
    val note: String? = null
)

data class MealPlanResponse(
    val id: Long,
    val familyId: Long,
    val date: LocalDate,
    val mealType: MealType,
    val note: String?,
    val createdBy: UserSimpleResponse,
    val itemCount: Int,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class MealPlanDetailResponse(
    val id: Long,
    val familyId: Long,
    val date: LocalDate,
    val mealType: MealType,
    val note: String?,
    val createdBy: UserSimpleResponse,
    val items: List<MealItemResponse>,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class MealItemResponse(
    val id: Long,
    val mealPlanId: Long,
    val dishName: String,
    val recipe: RecipeResponse?,
    val customDishName: String?,
    val servings: Int,
    val orderIndex: Int,
    val note: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class DailyMealPlanResponse(
    val date: LocalDate,
    val breakfast: MealPlanDetailResponse?,
    val lunch: MealPlanDetailResponse?,
    val dinner: MealPlanDetailResponse?,
    val snack: MealPlanDetailResponse?
)

data class WeeklyMealPlanResponse(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val days: List<DailyMealPlanResponse>
)

data class AddMealItemRequest(
    @field:NotNull(message = "Meal plan ID is required")
    val mealPlanId: Long,

    @field:Valid
    val item: CreateMealItemRequest
)

