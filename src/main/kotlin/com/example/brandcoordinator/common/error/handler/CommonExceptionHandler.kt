package com.example.brandcoordinator.common.error.handler

import com.example.brandcoordinator.common.error.BadRequestException
import com.example.brandcoordinator.common.error.NotFoundException
import com.example.brandcoordinator.common.error.dto.CommonErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommonExceptionHandler {

    @ExceptionHandler(
        BadRequestException::class,
        NotFoundException::class,
    )
    fun handleCommonException(e: Exception): ResponseEntity<CommonErrorResponse> {
        val status = when (e) {
            is BadRequestException -> HttpStatus.BAD_REQUEST
            is NotFoundException -> HttpStatus.NOT_FOUND
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        val errorResponse = CommonErrorResponse(message = e.message ?: "Unknown Error")
        return ResponseEntity(errorResponse, status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidateException(e: MethodArgumentNotValidException): ResponseEntity<MutableMap<String, String?>> {
        val errors = mutableMapOf<String, String?>()

        e.bindingResult.allErrors.forEach {
            val fieldName = (it as FieldError).field
            val errorMessage = it.defaultMessage
            errors[fieldName] = errorMessage
        }

        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }
}
