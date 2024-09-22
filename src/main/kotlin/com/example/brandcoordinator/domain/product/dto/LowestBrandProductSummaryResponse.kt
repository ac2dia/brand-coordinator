package com.example.brandcoordinator.domain.product.dto

import com.example.brandcoordinator.domain.product.model.Product

data class LowestBrandProductSummaryResponse(
    val brandName: String,
    val products: List<BrandProduct>,
    val amount: Int,
) {
    companion object {
        fun from(products: List<Product>): LowestBrandProductSummaryResponse {
            return LowestBrandProductSummaryResponse(
                brandName = products[0].brand.name,
                products = products.map {
                    BrandProduct(
                        category = it.category,
                        brandName = null,
                        price = it.price,
                    )
                },
                amount = products.sumOf { it.price }
            )
        }
    }
}
