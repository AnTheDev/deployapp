import 'package:json_annotation/json_annotation.dart';

part 'recipe_model.g.dart';

@JsonEnum()
enum Difficulty { EASY, MEDIUM, HARD }

@JsonSerializable()
class UserSimpleResponse {
  final int id;
  final String username;
  final String fullName;

  const UserSimpleResponse({
    required this.id,
    required this.username,
    required this.fullName,
  });

  factory UserSimpleResponse.fromJson(Map<String, dynamic> json) => _$UserSimpleResponseFromJson(json);
  Map<String, dynamic> toJson() => _$UserSimpleResponseToJson(this);
}

@JsonSerializable()
class Recipe {
  final int id;
  final String title;
  final String description;
  final String? imageUrl;
  @JsonKey(name: 'servings')
  final int? serves;
  final int? prepTime;
  final int? cookTime;
  final Difficulty difficulty;
  final bool isPublic;
  final UserSimpleResponse createdBy;
  final String createdAt;
  final String? instructions;

  const Recipe({
    required this.id,
    required this.title,
    required this.description,
    this.imageUrl,
    this.serves = 0,
    this.prepTime = 0,
    this.cookTime = 0,
    required this.difficulty,
    required this.isPublic,
    required this.createdBy,
    required this.createdAt,
    this.instructions,
  });

  // Helper getters to parse backend data
  List<String>? get steps {
    if (instructions == null || instructions!.isEmpty) return null;
    return instructions!.split('\n').where((s) => s.trim().isNotEmpty).toList();
  }

  factory Recipe.fromJson(Map<String, dynamic> json) => _$RecipeFromJson(json);
  Map<String, dynamic> toJson() => _$RecipeToJson(this);
}

@JsonSerializable()
class RecipeIngredient {
  final int id;
  final String ingredientName;
  final String? customIngredientName;
  final double quantity;
  final String unit;
  final String? note;
  final bool isOptional;

  const RecipeIngredient({
    required this.id,
    required this.ingredientName,
    this.customIngredientName,
    required this.quantity,
    required this.unit,
    this.note,
    required this.isOptional,
  });

  factory RecipeIngredient.fromJson(Map<String, dynamic> json) => _$RecipeIngredientFromJson(json);
  Map<String, dynamic> toJson() => _$RecipeIngredientToJson(this);
}

@JsonSerializable()
class RecipeDetail {
  final int id;
  final String title;
  final String description;
  final String? imageUrl;
  @JsonKey(name: 'servings')
  final int? serves;
  final int? prepTime;
  final int? cookTime;
  final Difficulty difficulty;
  final bool isPublic;
  final UserSimpleResponse createdBy;
  final String createdAt;
  final String? instructions;
  final List<RecipeIngredient> ingredients;

  const RecipeDetail({
    required this.id,
    required this.title,
    required this.description,
    this.imageUrl,
    this.serves = 0,
    this.prepTime = 0,
    this.cookTime = 0,
    required this.difficulty,
    required this.isPublic,
    required this.createdBy,
    required this.createdAt,
    this.instructions,
    required this.ingredients,
  });

  List<String>? get steps {
    if (instructions == null || instructions!.isEmpty) return null;
    return instructions!.split('\n').where((s) => s.trim().isNotEmpty).toList();
  }

  factory RecipeDetail.fromJson(Map<String, dynamic> json) => _$RecipeDetailFromJson(json);
  Map<String, dynamic> toJson() => _$RecipeDetailToJson(this);
}
