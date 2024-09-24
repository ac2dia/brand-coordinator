package com.example.brandcoordinator.domain.product

import com.example.brandcoordinator.domain.product.dto.BrandProductSummaryResponse
import com.example.brandcoordinator.domain.product.dto.CategoryBrandProductSummaryResponse
import com.example.brandcoordinator.domain.product.dto.LowestBrandProductSummaryResponse
import com.example.brandcoordinator.domain.product.dto.ProductPatchRequest
import com.example.brandcoordinator.domain.product.dto.ProductPostRequest
import com.example.brandcoordinator.domain.product.dto.ProductResponse
import jakarta.validation.Valid
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
    fun save(@Valid @RequestBody productPostRequest: ProductPostRequest): ResponseEntity<Void> {
        this.productService.save(productPostRequest = productPostRequest)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/api/v1/products/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @Valid @RequestBody productPatchRequest: ProductPatchRequest
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

    @GetMapping("/api/v1/get-summary-by-category/{category}")
    fun getSummaryByCategory(
        @PathVariable("category") category: String,
    ): ResponseEntity<CategoryBrandProductSummaryResponse> {
        val categoryPriceSummaryResponse =
            this.productService.findMaxAndMinProductsByCategory(category = category)
        return ResponseEntity.ok(categoryPriceSummaryResponse)
    }

    @GetMapping("/api/v1/get-summary-each-category")
    fun getSummaryEachCategory(): ResponseEntity<BrandProductSummaryResponse> {
        val brandProductSummaryResponse =
            this.productService.findLowestPriceProductsEachCategories()
        return ResponseEntity.ok(brandProductSummaryResponse)
    }

    @GetMapping("/api/v1/get-summary-lowest-brand")
    fun getSummaryLowestBrand(): ResponseEntity<LowestBrandProductSummaryResponse> {
        val lowestBrandProductSummaryResponse =
            this.productService.findLowestPriceProductsWithBrand()
        return ResponseEntity.ok(lowestBrandProductSummaryResponse)
    }
}
