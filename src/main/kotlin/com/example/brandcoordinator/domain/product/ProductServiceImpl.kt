package com.example.brandcoordinator.domain.product

import com.example.brandcoordinator.domain.brand.BrandRepository
import com.example.brandcoordinator.domain.brand.model.Brand
import com.example.brandcoordinator.domain.product.dto.BrandProductSummaryResponse
import com.example.brandcoordinator.domain.product.dto.CategoryBrandProductSummaryResponse
import com.example.brandcoordinator.domain.product.dto.LowestBrandProductSummaryResponse
import com.example.brandcoordinator.domain.product.dto.ProductPatchRequest
import com.example.brandcoordinator.domain.product.dto.ProductPostRequest
import com.example.brandcoordinator.domain.product.dto.ProductResponse
import com.example.brandcoordinator.domain.product.model.Product
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository,
) : ProductService {
    @Transactional(readOnly = true)
    override fun findAll(): List<ProductResponse> {
        val products = this.productRepository.findAll()
        return products.map { ProductResponse.from(product = it) }
    }

    @Transactional
    override fun save(productPostRequest: ProductPostRequest) {
        val brand = this.findBrandByName(name = productPostRequest.brandName)

        val product = Product(
            category = productPostRequest.category,
            brand = brand,
            price = productPostRequest.price,
        )
        this.productRepository.save(product)
    }

    @Transactional
    override fun update(
        id: Long,
        productPatchRequest: ProductPatchRequest
    ): ProductResponse {
        val product = this.findById(id = id)

        val brand = productPatchRequest.brandName?.let {
            this.findBrandByName(name = it)
        }

        product.update(
            category = productPatchRequest.category,
            brand = brand,
            price = productPatchRequest.price
        )
        this.productRepository.save(product)
        return ProductResponse.from(product = product)
    }

    @Transactional
    override fun delete(id: Long) {
        val product = findById(id = id)
        this.productRepository.delete(product)
    }

    @Transactional(readOnly = true)
    override fun findMaxAndMinProductsByCategory(category: String): CategoryBrandProductSummaryResponse {
        val products = this.productRepository.findByCategory(category = category)
            .ifEmpty { throw IllegalArgumentException("") }

        val maxPrice = products.maxOf { it.price }
        val maxPriceProducts = products.filter { it.price == maxPrice }

        val minPrice = products.minOf { it.price }
        val minPriceProducts = products.filter { it.price == minPrice }

        return CategoryBrandProductSummaryResponse.from(
            minimumPriceProducts = minPriceProducts,
            maximumPriceProducts = maxPriceProducts,
        )
    }

    @Transactional(readOnly = true)
    override fun findLowestPriceProductsEachCategories(): BrandProductSummaryResponse {
        val products = this.productRepository.findProductsWithLowestPrice()
        val filteredProducts = products
            .groupBy { it.category }
            .map { (_, productList) ->
                productList.maxWithOrNull(compareBy({ it.brand.name }))
            }
            .filterNotNull()
        return BrandProductSummaryResponse.from(products = filteredProducts)
    }

    @Transactional(readOnly = true)
    override fun findLowestPriceProductsWithBrand(): LowestBrandProductSummaryResponse {
        val products = this.productRepository.findProductsByBrandWithLowestTotalPrice()
        return LowestBrandProductSummaryResponse.from(products = products)
    }

    private fun findById(id: Long): Product =
        this.productRepository.findById(id).orElseThrow { NotFoundException() }

    private fun findBrandByName(name: String): Brand =
        this.brandRepository.findByName(name = name).orElseThrow { NotFoundException() }
}
