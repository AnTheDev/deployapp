package com.smartgrocery.exception

import com.smartgrocery.dto.common.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException): ResponseEntity<ApiResponse<Nothing>> {
        logger.warn("API Exception: ${ex.errorCode} - ${ex.message}")
        
        val status = when (ex) {
            is ResourceNotFoundException -> HttpStatus.NOT_FOUND
            is ValidationException -> HttpStatus.BAD_REQUEST
            is UnauthorizedException -> HttpStatus.UNAUTHORIZED
            is ForbiddenException -> HttpStatus.FORBIDDEN
            is ConflictException -> HttpStatus.CONFLICT
            is ConcurrencyException -> HttpStatus.CONFLICT
            is AuthenticationException -> HttpStatus.UNAUTHORIZED
            else -> HttpStatus.BAD_REQUEST
        }

        return ResponseEntity
            .status(status)
            .body(ApiResponse.error(ex.errorCode.code, ex.message))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Map<String, String>>> {
        val errors = ex.bindingResult.allErrors.associate { error ->
            val fieldName = (error as? FieldError)?.field ?: error.objectName
            val errorMessage = error.defaultMessage ?: "Invalid value"
            fieldName to errorMessage
        }

        logger.warn("Validation errors: $errors")

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse(
                code = ErrorCode.VALIDATION_ERROR.code,
                message = "Validation failed",
                data = errors
            ))
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException::class)
    fun handleOptimisticLockingException(ex: ObjectOptimisticLockingFailureException): ResponseEntity<ApiResponse<Nothing>> {
        logger.warn("Optimistic locking failure: ${ex.message}")
        
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ApiResponse.error(
                ErrorCode.CONCURRENCY_ERROR.code,
                "The resource was modified by another user. Please refresh and try again."
            ))
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<ApiResponse<Nothing>> {
        logger.error("Data integrity violation: ${ex.message}")
        
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ApiResponse.error(
                ErrorCode.CONFLICT.code,
                "Data integrity violation. The operation could not be completed."
            ))
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<ApiResponse<Nothing>> {
        logger.warn("Bad credentials: ${ex.message}")
        
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(
                ErrorCode.INVALID_CREDENTIALS.code,
                ErrorCode.INVALID_CREDENTIALS.message
            ))
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException): ResponseEntity<ApiResponse<Nothing>> {
        logger.warn("Authentication failed: ${ex.message}")
        
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(
                ErrorCode.UNAUTHORIZED.code,
                ex.message ?: ErrorCode.UNAUTHORIZED.message
            ))
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<ApiResponse<Nothing>> {
        logger.warn("Access denied: ${ex.message}")
        
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.error(
                ErrorCode.FORBIDDEN.code,
                ErrorCode.FORBIDDEN.message
            ))
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<ApiResponse<Nothing>> {
        logger.warn("Type mismatch: ${ex.message}")
        
        val message = "Invalid value '${ex.value}' for parameter '${ex.name}'"
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ErrorCode.INVALID_REQUEST.code, message))
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        logger.error("Unexpected error: ${ex.message}", ex)
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(
                ErrorCode.INTERNAL_ERROR.code,
                "An unexpected error occurred. Please try again later."
            ))
    }
}

