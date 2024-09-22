package com.example.brandcoordinator.domain.product.dto

import com.example.brandcoordinator.domain.product.model.Product

data class ProductResponse(
    val id: Long,
    val category: String,
    val brandName: String,
    val price: Int,
) {
    companion object {
        fun from(product: Product): ProductResponse =
            ProductResponse(
                id = product.id,
                category = product.category,
                brandName = product.brand.name,
                price = product.price,
            )
    }
}
