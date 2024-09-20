package com.example.brandcoordinator.domain.brand.dto

import com.example.brandcoordinator.domain.brand.model.Brand

data class BrandResponse(
    val id: Long,
    val name: String,
) {
    companion object {
        fun from(brand: Brand): BrandResponse {
            return BrandResponse(
                id = brand.id,
                name = brand.name
            )
        }
    }
}
