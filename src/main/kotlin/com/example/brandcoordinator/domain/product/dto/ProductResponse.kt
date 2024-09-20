package com.example.brandcoordinator.domain.product.dto

data class ProductResponse(
    val id: Long,
    val category: String,
    val brandName: String,
    val price: Int,
)
