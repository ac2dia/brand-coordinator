package com.example.brandcoordinator.domain.product

import com.example.brandcoordinator.domain.product.dto.ProductPatchRequest
import com.example.brandcoordinator.domain.product.dto.ProductPostRequest
import com.example.brandcoordinator.domain.product.dto.ProductResponse

interface ProductService {
    fun findAll(): List<ProductResponse>
    fun save(product: ProductPostRequest)
    fun update(product: ProductPatchRequest): ProductResponse
    fun delete(id: Long)
}
