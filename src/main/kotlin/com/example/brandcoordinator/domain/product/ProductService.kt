package com.example.brandcoordinator.domain.product

import com.example.brandcoordinator.domain.product.dto.CategoryBrandProductSummaryResponse
import com.example.brandcoordinator.domain.product.dto.ProductPatchRequest
import com.example.brandcoordinator.domain.product.dto.ProductPostRequest
import com.example.brandcoordinator.domain.product.dto.ProductResponse

interface ProductService {
    fun findAll(): List<ProductResponse>
    fun save(productPostRequest: ProductPostRequest)
    fun update(id: Long, productPatchRequest: ProductPatchRequest): ProductResponse
    fun delete(id: Long)
    fun findMaxAndMinProductsByCategory(category: String): CategoryBrandProductSummaryResponse
}
