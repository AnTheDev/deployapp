package com.smartgrocery.controller

import com.smartgrocery.dto.common.ApiResponse
import com.smartgrocery.dto.common.PageResponse
import com.smartgrocery.dto.recipe.*
import com.smartgrocery.service.RecipeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/recipes")
@Tag(name = "Recipes", description = "Recipe management APIs")
class RecipeController(
    private val recipeService: RecipeService
) {

    @GetMapping
    @Operation(summary = "Get all public recipes with pagination")
    fun getAllRecipes(
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<ApiResponse<PageResponse<RecipeResponse>>> {
        val recipes = recipeService.getAllRecipes(pageable)
        return ResponseEntity.ok(ApiResponse.success(recipes))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by ID with ingredients")
    fun getRecipeById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<RecipeDetailResponse>> {
        val recipe = recipeService.getRecipeById(id)
        return ResponseEntity.ok(ApiResponse.success(recipe))
    }

    @GetMapping("/search")
    @Operation(summary = "Search recipes by title")
    fun searchRecipes(
        @RequestParam title: String
    ): ResponseEntity<ApiResponse<List<RecipeResponse>>> {
        val recipes = recipeService.searchRecipes(title)
        return ResponseEntity.ok(ApiResponse.success(recipes))
    }

    @GetMapping("/my-recipes")
    @Operation(summary = "Get recipes created by current user")
    fun getMyRecipes(): ResponseEntity<ApiResponse<List<RecipeResponse>>> {
        val recipes = recipeService.getMyRecipes()
        return ResponseEntity.ok(ApiResponse.success(recipes))
    }

    @GetMapping("/suggestions/{familyId}")
    @Operation(summary = "Get recipe suggestions based on available fridge items")
    fun suggestRecipes(
        @PathVariable familyId: Long
    ): ResponseEntity<ApiResponse<List<RecipeSuggestionResponse>>> {
        val suggestions = recipeService.suggestRecipes(familyId)
        return ResponseEntity.ok(ApiResponse.success(suggestions))
    }

    @PostMapping
    @Operation(summary = "Create a new recipe")
    fun createRecipe(
        @Valid @RequestBody request: CreateRecipeRequest
    ): ResponseEntity<ApiResponse<RecipeDetailResponse>> {
        val recipe = recipeService.createRecipe(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.created(recipe, "Recipe created successfully"))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a recipe (owner only)")
    fun updateRecipe(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateRecipeRequest
    ): ResponseEntity<ApiResponse<RecipeDetailResponse>> {
        val recipe = recipeService.updateRecipe(id, request)
        return ResponseEntity.ok(ApiResponse.success(recipe, "Recipe updated successfully"))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a recipe (owner only)")
    fun deleteRecipe(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<Nothing>> {
        recipeService.deleteRecipe(id)
        return ResponseEntity.ok(ApiResponse.success("Recipe deleted successfully"))
    }
}

