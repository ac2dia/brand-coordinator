package com.example.brandcoordinator.domain.product.dto

import com.example.brandcoordinator.domain.product.model.Product

data class CategoryBrandProductSummaryResponse(
    val category: String,
    val minimum: List<BrandProduct>,
    val maximum: List<BrandProduct>,
) {
    companion object {
        fun from(
            minimumPriceProducts: List<Product>,
            maximumPriceProducts: List<Product>
        ): CategoryBrandProductSummaryResponse =
            CategoryBrandProductSummaryResponse(
                category = minimumPriceProducts[0].category,
                minimum = minimumPriceProducts.map {
                    BrandProduct(
                        category = null,
                        brandName = it.brand.name,
                        price = it.price,
                    )
                },
                maximum = maximumPriceProducts.map {
                    BrandProduct(
                        category = null,
                        brandName = it.brand.name,
                        price = it.price,
                    )
                },
            )
    }
}
