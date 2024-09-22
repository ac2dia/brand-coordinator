package com.example.brandcoordinator.domain.product.dto

import com.example.brandcoordinator.domain.product.model.Product

data class BrandProductSummaryResponse(
    val amount: Int,
    val products: List<BrandProduct>
) {
    companion object {
        fun from(products: List<Product>): BrandProductSummaryResponse =
            BrandProductSummaryResponse(
                amount = products.sumOf { it.price },
                products = products.map {
                    BrandProduct(
                        category = it.category,
                        brandName = it.brand.name,
                        price = it.price,
                    )
                }
            )
    }
}
