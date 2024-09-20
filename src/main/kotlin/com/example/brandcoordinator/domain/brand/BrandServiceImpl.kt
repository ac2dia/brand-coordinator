package com.example.brandcoordinator.domain.brand

import com.example.brandcoordinator.domain.brand.dto.BrandPatchRequest
import com.example.brandcoordinator.domain.brand.dto.BrandPostRequest
import com.example.brandcoordinator.domain.brand.dto.BrandResponse
import com.example.brandcoordinator.domain.brand.model.Brand
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service

@Service
class BrandServiceImpl(
    private val brandRepository: BrandRepository,
) : BrandService {
    override fun findAll(): List<BrandResponse> {
        val brands = this.brandRepository.findAll()
        return brands.map { BrandResponse.from(brand = it) }
    }

    override fun save(brandPostRequest: BrandPostRequest) {
        val brand = Brand.from(brandPostRequest = brandPostRequest)
        this.brandRepository.save(brand)
    }

    override fun update(id: Long, brandPatchRequest: BrandPatchRequest): BrandResponse {
        val brand = findById(id)
        brand.update(name = brandPatchRequest.name)

        return BrandResponse.from(brand = brand)
    }

    override fun delete(id: Long) {
        val brand = findById(id)
        this.brandRepository.delete(brand)
    }

    private fun findById(id: Long): Brand {
        return this.brandRepository.findById(id).orElseThrow { NotFoundException() }
    }
}
