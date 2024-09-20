package com.example.brandcoordinator.domain.brand

import com.example.brandcoordinator.domain.brand.dto.BrandPatchRequest
import com.example.brandcoordinator.domain.brand.dto.BrandPostRequest
import com.example.brandcoordinator.domain.brand.dto.BrandResponse

interface BrandService {
  fun findAll(): List<BrandResponse>
  fun save(brand: BrandPostRequest)
  fun update(brand: BrandPatchRequest): BrandResponse
  fun delete(id: String)
}
