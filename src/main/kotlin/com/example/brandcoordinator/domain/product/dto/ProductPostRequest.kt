package com.example.brandcoordinator.domain.product.dto

data class ProductPostRequest(
    val category: String,
    val brandName: String,
    val price: Int,
)
