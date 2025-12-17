package com.smartgrocery.dto.common

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T?, message: String = "Success"): ApiResponse<T> {
            return ApiResponse(code = 1000, message = message, data = data)
        }

        fun <T> success(message: String = "Success"): ApiResponse<T> {
            return ApiResponse(code = 1000, message = message, data = null)
        }

        fun <T> error(code: Int, message: String): ApiResponse<T> {
            return ApiResponse(code = code, message = message, data = null)
        }

        fun <T> created(data: T?, message: String = "Created successfully"): ApiResponse<T> {
            return ApiResponse(code = 1001, message = message, data = data)
        }
    }
}

