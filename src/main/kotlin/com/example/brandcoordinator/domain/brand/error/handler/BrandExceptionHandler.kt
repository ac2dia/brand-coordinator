package com.example.brandcoordinator.domain.brand.error.handler

import com.example.brandcoordinator.common.error.dto.CommonErrorResponse
import com.example.brandcoordinator.domain.brand.error.AlreadyExistBrandException
import com.example.brandcoordinator.domain.brand.error.BrandIsUsedByProductException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BrandExceptionHandler {

    @ExceptionHandler(
        AlreadyExistBrandException::class,
        BrandIsUsedByProductException::class,
    )
    fun handleCustomException(e: Exception): ResponseEntity<CommonErrorResponse> {
        val status = when (e) {
            is AlreadyExistBrandException -> HttpStatus.BAD_REQUEST
            is BrandIsUsedByProductException -> HttpStatus.BAD_REQUEST
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        val errorResponse = CommonErrorResponse(message = e.message ?: "Unknown Error")
        return ResponseEntity(errorResponse, status)
    }
}
