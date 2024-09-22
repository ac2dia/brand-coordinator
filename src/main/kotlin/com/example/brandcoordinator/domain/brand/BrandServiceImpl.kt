package com.example.brandcoordinator.domain.brand

import com.example.brandcoordinator.domain.brand.dto.BrandPatchRequest
import com.example.brandcoordinator.domain.brand.dto.BrandPostRequest
import com.example.brandcoordinator.domain.brand.dto.BrandResponse
import com.example.brandcoordinator.domain.brand.model.Brand
import com.example.brandcoordinator.domain.product.ProductRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service

@Service
class BrandServiceImpl(
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository,
) : BrandService {
    override fun findAll(): List<BrandResponse> {
        val brands = this.brandRepository.findAll()
        return brands.map { BrandResponse.from(brand = it) }
    }

    override fun save(brandPostRequest: BrandPostRequest) {
        val brand = Brand.from(brandPostRequest = brandPostRequest)
        this.brandRepository.save(brand)
    }

    override fun update(
        id: Long,
        brandPatchRequest: BrandPatchRequest
    ): BrandResponse {
        val brand = findById(id = id)
        brand.update(name = brandPatchRequest.name)
        this.brandRepository.save(brand)

        return BrandResponse.from(brand = brand)
    }

    override fun delete(id: Long) {
        if(this.productRepository.findByBrandId(brandId = id).isNotEmpty()) {
            throw IllegalArgumentException("")
        }

        val brand = findById(id = id)
        this.brandRepository.delete(brand)
    }

    private fun findById(id: Long): Brand =
        this.brandRepository.findById(id).orElseThrow { NotFoundException() }
}
