package com.example.brandcoordinator.domain.product.dto

import jakarta.validation.constraints.PositiveOrZero

data class ProductPatchRequest(
    val category: String?,
    val brandName: String?,
    @field:PositiveOrZero(message = "price must be positive or zero")
    val price: Int?,
)
