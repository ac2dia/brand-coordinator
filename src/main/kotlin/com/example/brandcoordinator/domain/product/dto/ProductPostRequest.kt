package com.example.brandcoordinator.domain.product.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero

data class ProductPostRequest(
    @field:NotBlank(message = "category is required")
    val category: String,
    @field:NotBlank(message = "brandName is required")
    val brandName: String,
    @field:PositiveOrZero(message = "price must be positive or zero")
    val price: Int,
)
