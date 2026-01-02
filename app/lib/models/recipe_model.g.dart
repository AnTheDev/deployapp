// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'recipe_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

UserSimpleResponse _$UserSimpleResponseFromJson(Map<String, dynamic> json) =>
    UserSimpleResponse(
      id: (json['id'] as num).toInt(),
      username: json['username'] as String,
      fullName: json['fullName'] as String,
    );

Map<String, dynamic> _$UserSimpleResponseToJson(UserSimpleResponse instance) =>
    <String, dynamic>{
      'id': instance.id,
      'username': instance.username,
      'fullName': instance.fullName,
    };

Recipe _$RecipeFromJson(Map<String, dynamic> json) => Recipe(
      id: (json['id'] as num).toInt(),
      title: json['title'] as String,
      description: json['description'] as String,
      imageUrl: json['imageUrl'] as String?,
      serves: (json['servings'] as num?)?.toInt() ?? 0,
      prepTime: (json['prepTime'] as num?)?.toInt() ?? 0,
      cookTime: (json['cookTime'] as num?)?.toInt() ?? 0,
      difficulty: $enumDecode(_$DifficultyEnumMap, json['difficulty']),
      isPublic: json['isPublic'] as bool,
      notes: json['notes'] as String?,
      createdBy: UserSimpleResponse.fromJson(
          json['createdBy'] as Map<String, dynamic>),
      createdAt: json['createdAt'] as String,
      instructions: json['instructions'] as String?,
    );

Map<String, dynamic> _$RecipeToJson(Recipe instance) => <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'description': instance.description,
      'imageUrl': instance.imageUrl,
      'servings': instance.serves,
      'prepTime': instance.prepTime,
      'cookTime': instance.cookTime,
      'difficulty': _$DifficultyEnumMap[instance.difficulty]!,
      'isPublic': instance.isPublic,
      'notes': instance.notes,
      'createdBy': instance.createdBy,
      'createdAt': instance.createdAt,
      'instructions': instance.instructions,
    };

const _$DifficultyEnumMap = {
  Difficulty.EASY: 'EASY',
  Difficulty.MEDIUM: 'MEDIUM',
  Difficulty.HARD: 'HARD',
};

RecipeIngredient _$RecipeIngredientFromJson(Map<String, dynamic> json) =>
    RecipeIngredient(
      id: (json['id'] as num).toInt(),
      ingredientName: json['ingredientName'] as String,
      customIngredientName: json['customIngredientName'] as String?,
      quantity: (json['quantity'] as num).toDouble(),
      unit: json['unit'] as String,
      note: json['note'] as String?,
      isOptional: json['isOptional'] as bool,
    );

Map<String, dynamic> _$RecipeIngredientToJson(RecipeIngredient instance) =>
    <String, dynamic>{
      'id': instance.id,
      'ingredientName': instance.ingredientName,
      'customIngredientName': instance.customIngredientName,
      'quantity': instance.quantity,
      'unit': instance.unit,
      'note': instance.note,
      'isOptional': instance.isOptional,
    };

RecipeDetail _$RecipeDetailFromJson(Map<String, dynamic> json) => RecipeDetail(
      id: (json['id'] as num).toInt(),
      title: json['title'] as String,
      description: json['description'] as String,
      imageUrl: json['imageUrl'] as String?,
      serves: (json['servings'] as num?)?.toInt() ?? 0,
      prepTime: (json['prepTime'] as num?)?.toInt() ?? 0,
      cookTime: (json['cookTime'] as num?)?.toInt() ?? 0,
      difficulty: $enumDecode(_$DifficultyEnumMap, json['difficulty']),
      isPublic: json['isPublic'] as bool,
      notes: json['notes'] as String?,
      createdBy: UserSimpleResponse.fromJson(
          json['createdBy'] as Map<String, dynamic>),
      createdAt: json['createdAt'] as String,
      instructions: json['instructions'] as String?,
      ingredients: (json['ingredients'] as List<dynamic>)
          .map((e) => RecipeIngredient.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$RecipeDetailToJson(RecipeDetail instance) =>
    <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'description': instance.description,
      'imageUrl': instance.imageUrl,
      'servings': instance.serves,
      'prepTime': instance.prepTime,
      'cookTime': instance.cookTime,
      'difficulty': _$DifficultyEnumMap[instance.difficulty]!,
      'isPublic': instance.isPublic,
      'notes': instance.notes,
      'createdBy': instance.createdBy,
      'createdAt': instance.createdAt,
      'instructions': instance.instructions,
      'ingredients': instance.ingredients,
    };
