package com.example.brandcoordinator.domain.product

import com.example.brandcoordinator.domain.product.dto.CategoryPricingSummaryResponse
import com.example.brandcoordinator.domain.product.dto.ProductPatchRequest
import com.example.brandcoordinator.domain.product.dto.ProductPostRequest
import com.example.brandcoordinator.domain.product.dto.ProductResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    val productService: ProductService,
) {
    @GetMapping("/api/v1/products")
    fun findAll(): ResponseEntity<List<ProductResponse>> {
        val products = this.productService.findAll()
        return ResponseEntity.ok(products)
    }

    @PostMapping("/api/v1/products")
    fun save(@RequestBody productPostRequest: ProductPostRequest): ResponseEntity<Void> {
        this.productService.save(productPostRequest = productPostRequest)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/api/v1/products/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody productPatchRequest: ProductPatchRequest
    ): ResponseEntity<ProductResponse> {
        val modifiedProduct = this.productService.update(
            id = id,
            productPatchRequest = productPatchRequest,
        )
        return ResponseEntity.ok(modifiedProduct)
    }

    @DeleteMapping("/api/v1/products/{id}")
    fun delete(
        @PathVariable("id") id: Long
    ): ResponseEntity<Void> {
        this.productService.delete(id = id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/api/v1/get-price-summary-by-category/{name}")
    fun getPriceSummaryByCategory(
        @PathVariable("name") name: String,
    ): ResponseEntity<CategoryPricingSummaryResponse> {
        val categoryPriceSummaryResponse =
            this.productService.findMaxAndMinProductsByCategory(category = name)
        return ResponseEntity.ok(categoryPriceSummaryResponse)
    }
}
