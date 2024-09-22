package com.example.brandcoordinator.domain.product.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BrandProduct(
    val category: String?,
    val brandName: String,
    val price: Int,
)
