package com.example.brandcoordinator.domain.product.dto

import com.example.brandcoordinator.domain.product.model.Product

data class CategoryPricingSummaryResponse(
    val category: String,
    val minimum: BrandPriceDetails,
    val maximum: BrandPriceDetails,
) {
    companion object {
        fun from(minimumPriceProduct: Product, maximumPriceProduct: Product): CategoryPricingSummaryResponse {
            return CategoryPricingSummaryResponse(
                category = minimumPriceProduct.category,
                minimum = BrandPriceDetails(
                    brandName = minimumPriceProduct.brand.name,
                    price = minimumPriceProduct.price,
                ),
                maximum = BrandPriceDetails(
                    brandName = maximumPriceProduct.brand.name,
                    price = maximumPriceProduct.price,
                )
            )
        }
    }
}
