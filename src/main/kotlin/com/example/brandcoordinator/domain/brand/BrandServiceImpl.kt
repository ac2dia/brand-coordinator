package com.example.brandcoordinator.domain.brand

import com.example.brandcoordinator.common.error.NotFoundException
import com.example.brandcoordinator.domain.brand.dto.BrandPatchRequest
import com.example.brandcoordinator.domain.brand.dto.BrandPostRequest
import com.example.brandcoordinator.domain.brand.dto.BrandResponse
import com.example.brandcoordinator.domain.brand.error.AlreadyExistBrandException
import com.example.brandcoordinator.domain.brand.error.BrandIsUsedByProductException
import com.example.brandcoordinator.domain.brand.model.Brand
import com.example.brandcoordinator.domain.product.ProductRepository

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrandServiceImpl(
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository,
) : BrandService {
    @Transactional(readOnly = true)
    override fun findAll(): List<BrandResponse> {
        val brands = this.brandRepository.findAll()
        return brands.map { BrandResponse.from(brand = it) }
    }

    @Transactional
    override fun save(brandPostRequest: BrandPostRequest) {
        this.validateBrandNameIsUnique(name = brandPostRequest.name)
        
        val brand = Brand.from(brandPostRequest = brandPostRequest)
        this.brandRepository.save(brand)
    }

    @Transactional
    override fun update(
        id: Long,
        brandPatchRequest: BrandPatchRequest
    ): BrandResponse {
        this.validateBrandNameIsUnique(name = brandPatchRequest.name)

        val brand = findById(id = id)
        brand.update(name = brandPatchRequest.name)
        this.brandRepository.save(brand)

        return BrandResponse.from(brand = brand)
    }

    @Transactional
    override fun delete(id: Long) {
        val isBrandUsedByProduct = this.productRepository.findByBrandId(brandId = id).isNotEmpty()
        if(isBrandUsedByProduct) {
            throw BrandIsUsedByProductException("$id Brand is used by product")
        }

        val brand = findById(id = id)
        this.brandRepository.delete(brand)
    }

    private fun findById(id: Long): Brand =
        this.brandRepository.findById(id).orElseThrow { NotFoundException("$id Brand is not found by id") }

    private fun validateBrandNameIsUnique(name: String) {
        if (brandRepository.existsByName(name)) {
            throw AlreadyExistBrandException("Brand with name $name already exists")
        }
    }
}
