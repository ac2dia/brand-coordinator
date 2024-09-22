package com.example.brandcoordinator.domain.product.dto

data class ProductPatchRequest(
    val category: String?,
    val brandName: String?,
    val price: Int?,
)
