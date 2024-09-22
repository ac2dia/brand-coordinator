package com.example.brandcoordinator.domain.product.dto

import com.example.brandcoordinator.domain.product.model.Product

data class CategoryPricingSummaryResponse(
    val category: String,
    val minimum: List<BrandPriceDetails>,
    val maximum: List<BrandPriceDetails>,
) {
    companion object {
        fun from(
            minimumPriceProducts: List<Product>,
            maximumPriceProducts: List<Product>
        ): CategoryPricingSummaryResponse {
            return CategoryPricingSummaryResponse(
                category = minimumPriceProducts[0].category,
                minimum = minimumPriceProducts.map {
                    BrandPriceDetails(
                        brandName = it.brand.name,
                        price = it.price,
                    )
                },
                maximum = maximumPriceProducts.map {
                    BrandPriceDetails(
                        brandName = it.brand.name,
                        price = it.price,
                    )
                },
            )
        }
    }
}
