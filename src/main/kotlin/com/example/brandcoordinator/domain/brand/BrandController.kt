package com.example.brandcoordinator.domain.brand

import com.example.brandcoordinator.domain.brand.dto.BrandPatchRequest
import com.example.brandcoordinator.domain.brand.dto.BrandPostRequest
import com.example.brandcoordinator.domain.brand.dto.BrandResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BrandController(
    private val brandService: BrandService,
) {
    @GetMapping("/api/v1/brands")
    fun findAll(): ResponseEntity<List<BrandResponse>> {
        val brands = this.brandService.findAll()
        return ResponseEntity.ok(brands)
    }

    @PostMapping("/api/v1/brands")
    fun save(@RequestBody brandPostRequest: BrandPostRequest): ResponseEntity<Void> {
        this.brandService.save(brandPostRequest = brandPostRequest)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/api/v1/brands/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody brandPatchRequest: BrandPatchRequest
    ): ResponseEntity<BrandResponse> {
        val modifiedBrand = this.brandService.update(
            id = id,
            brandPatchRequest = brandPatchRequest,
        )
        return ResponseEntity.ok(modifiedBrand)
    }

    @DeleteMapping("/api/v1/brands/{id}")
    fun delete(
        @PathVariable("id") id: Long
    ): ResponseEntity<Void> {
        this.brandService.delete(id = id)
        return ResponseEntity.noContent().build()
    }
}
