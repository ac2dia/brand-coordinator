package com.example.brandcoordinator.domain.brand

import com.example.brandcoordinator.domain.brand.dto.BrandPatchRequest
import com.example.brandcoordinator.domain.brand.dto.BrandPostRequest
import com.example.brandcoordinator.domain.brand.error.AlreadyExistBrandException
import com.example.brandcoordinator.domain.brand.error.BrandIsUsedByProductException
import com.example.brandcoordinator.domain.brand.model.Brand
import com.example.brandcoordinator.domain.product.ProductRepository
import com.example.brandcoordinator.domain.product.model.Product
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import java.util.Optional


class BrandServiceImplTest : BehaviorSpec({
    // Mock repositories
    val productRepository = mockk<ProductRepository>()
    val brandRepository = mockk<BrandRepository>()

    // Service implementation
    val brandService = BrandServiceImpl(
        productRepository = productRepository,
        brandRepository = brandRepository
    )

    afterEach {
        unmockkAll()
    }

    Given("a brand exists") {
        val aBrand = Brand(id = 1L, name = "A")
        val bBrand = Brand(id = 2L, name = "B")

        When("findAll is called") {
            every { brandRepository.findAll() } returns listOf(aBrand, bBrand)

            val results = brandService.findAll()

            Then("it should return all brands as BrandResponse") {
                results.size shouldBe 2
            }
        }

        When("save is called with same name brand") {
            val brandPostRequest = BrandPostRequest(name = "A")

            every { brandRepository.existsByName(any()) } returns true
            every { brandRepository.save(any()) } returns aBrand

            Then("it should throw AlreadyExistBrandException due to same name brand") {
                shouldThrow<AlreadyExistBrandException> {
                    brandService.save(brandPostRequest)
                }
            }
        }

        When("save is called") {
            val brandPostRequest = BrandPostRequest(name = "A")

            every { brandRepository.existsByName(any()) } returns false
            every { brandRepository.save(any()) } returns aBrand

            brandService.save(brandPostRequest)

            Then("it should save the new brand") {
                verify { brandRepository.save(any()) }
            }
        }

        When("update is called with same name brand") {
            val existingBrand = Brand(id = 3L, name = "C")
            val patchRequest = BrandPatchRequest(name = "D")

            every { brandRepository.existsByName(any()) } returns true
            every { brandRepository.findById(3L) } returns Optional.of(existingBrand)
            every { brandRepository.save(any()) } returns existingBrand

            Then("it should throw AlreadyExistBrandException due to same name brand") {
                shouldThrow<AlreadyExistBrandException> {
                    brandService.update(id = 3L, brandPatchRequest = patchRequest)
                }
            }
        }

        When("update is called") {
            val existingBrand = Brand(id = 3L, name = "C")
            val patchRequest = BrandPatchRequest(name = "D")

            every { brandRepository.existsByName(any()) } returns false
            every { brandRepository.findById(3L) } returns Optional.of(existingBrand)
            every { brandRepository.save(any()) } returns existingBrand

            val updatedBrand = brandService.update(id = 3L, brandPatchRequest = patchRequest)

            Then("it should update and return the updated brand") {
                updatedBrand.name shouldBe patchRequest.name
                verify { brandRepository.save(existingBrand) }
            }
        }

        When("delete is called with associated products") {
            val existingBrand = Brand(id = 3L, name = "C")
            val associatedProducts = listOf(Product(id = 1L, brand = existingBrand, category = "상의", price = 4000))

            every { productRepository.findByBrandId(3L) } returns associatedProducts

            Then("it should throw BrandIsUsedByProductException due to associated products") {
                shouldThrow<BrandIsUsedByProductException> {
                    brandService.delete(3L)
                }
            }
        }

        When("delete is called without associated products") {
            val existingBrand = Brand(id = 3L, name = "C")

            every { productRepository.findByBrandId(3L) } returns emptyList()
            every { brandRepository.findById(3L) } returns Optional.of(existingBrand)
            every { brandRepository.delete(existingBrand) } returns Unit

            brandService.delete(3L)

            Then("it should delete the brand") {
                verify { brandRepository.delete(existingBrand) }
            }
        }
    }
})

