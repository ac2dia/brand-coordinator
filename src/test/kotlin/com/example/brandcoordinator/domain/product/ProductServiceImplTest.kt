package com.example.brandcoordinator.domain.product

import com.example.brandcoordinator.common.error.NotFoundException
import com.example.brandcoordinator.domain.brand.BrandRepository
import com.example.brandcoordinator.domain.brand.model.Brand
import com.example.brandcoordinator.domain.product.dto.ProductPatchRequest
import com.example.brandcoordinator.domain.product.dto.ProductPostRequest
import com.example.brandcoordinator.domain.product.model.Product
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import java.util.Optional


class ProductServiceImplTest : BehaviorSpec({
    // Mock repositories
    val productRepository = mockk<ProductRepository>()
    val brandRepository = mockk<BrandRepository>()

    // Service implementation
    val productService = ProductServiceImpl(
        productRepository = productRepository,
        brandRepository = brandRepository
    )

    afterEach {
        unmockkAll()
    }

    Given("a product exists") {
        val aBrand = Brand(id = 1L, name = "A")
        val bBrand = Brand(id = 2L, name = "B")

        val product = Product(
            id = 1L,
            category = "상의",
            brand = aBrand,
            price = 5000
        )
        val aBrandTopProduct = Product(
            id = 2L,
            category = "상의",
            brand = aBrand,
            price = 4000
        )
        val bBrandTopProduct = Product(
            id = 3L,
            category = "상의",
            brand = bBrand,
            price = 6000
        )
        val aBrandBottomProduct = Product(
            id = 4L,
            category = "하의",
            brand = aBrand,
            price = 10000
        )

        When("findAll is called") {
            every { productRepository.findAll() } returns listOf(product)

            val result = productService.findAll()

            Then("it should return a list of ProductResponse") {
                result.size shouldBe 1
                result[0].category shouldBe product.category
                result[0].brandName shouldBe product.brand.name
                result[0].price shouldBe product.price
            }
        }

        When("save is called with not exist brand name") {
            val productPostRequest = ProductPostRequest(
                category = "모자",
                brandName = "A",
                price = 5000
            )
            every { brandRepository.findByName("A") } returns Optional.empty()
            every { productRepository.save(any()) } returns product

            Then("it should throw NotFoundException due to not exist brand name") {
                shouldThrow<NotFoundException> {
                    productService.save(productPostRequest)
                }
            }
        }

        When("save is called with valid data") {
            val brand = Brand(id = 1L, name = "A")
            val productPostRequest = ProductPostRequest(
                category = "모자",
                brandName = "A",
                price = 5000
            )
            every { brandRepository.findByName("A") } returns Optional.of(brand)
            every { productRepository.save(any()) } returns product

            productService.save(productPostRequest)

            Then("it should save the product") {
                verify { productRepository.save(any<Product>()) }
            }
        }

        When("update is called with not exist brand name") {
            val productPatchRequest = ProductPatchRequest(
                category = "하의",
                brandName = "A",
                price = 6000
            )

            every { productRepository.findById(1L) } returns Optional.of(product)
            every { brandRepository.findByName("A") } returns Optional.empty()
            every { productRepository.save(any()) } returns product

            Then("it should throw NotFoundException due to not exist brand name") {
                shouldThrow<NotFoundException> {
                    productService.update(1L, productPatchRequest)
                }
            }
        }

        When("update is called with not exist product id") {
            val brand = Brand(id = 1L, name = "A")
            val productPatchRequest = ProductPatchRequest(
                category = "하의",
                brandName = "A",
                price = 6000
            )

            every { productRepository.findById(1L) } returns Optional.empty()
            every { brandRepository.findByName("A") } returns Optional.of(brand)
            every { productRepository.save(any()) } returns product

            Then("it should throw NotFoundException due to not exist brand name") {
                shouldThrow<NotFoundException> {
                    productService.update(1L, productPatchRequest)
                }
            }
        }

        When("update is called with valid data") {
            val brand = Brand(id = 1L, name = "A")
            val productPatchRequest = ProductPatchRequest(
                category = "하의",
                brandName = "A",
                price = 6000
            )

            every { productRepository.findById(1L) } returns Optional.of(product)
            every { brandRepository.findByName("A") } returns Optional.of(brand)
            every { productRepository.save(any()) } returns product

            val result = productService.update(1L, productPatchRequest)

            Then("it should update and return the updated ProductResponse") {
                result.category shouldBe product.category
                result.brandName shouldBe product.brand.name
                result.price shouldBe product.price
                verify { productRepository.save(any<Product>()) }
            }
        }

        When("delete is called with invalid id") {
            every { productRepository.findById(1L) } returns Optional.empty()
            every { productRepository.delete(any()) } just Runs

            Then("it should throw NotFoundException due to not exist product id") {
                shouldThrow<NotFoundException> {
                    productService.delete(1L)
                }
            }
        }


        When("delete is called with valid id") {
            every { productRepository.findById(1L) } returns Optional.of(product)
            every { productRepository.delete(any()) } just Runs

            productService.delete(1L)

            Then("it should delete the product") {
                verify { productRepository.delete(any<Product>()) }
            }
        }

        When("findMaxAndMinProductsByCategory is called with a category") {
            every { productRepository.findByCategory("상의") } returns listOf(aBrandTopProduct, bBrandTopProduct)

            val result = productService.findMaxAndMinProductsByCategory("상의")

            Then("it should return max and min priced products") {
                result.category shouldBe aBrandTopProduct.category
                result.minimum[0].brandName shouldBe aBrandTopProduct.brand.name
                result.minimum[0].price shouldBe aBrandTopProduct.price
                result.maximum[0].brandName shouldBe bBrandTopProduct.brand.name
                result.maximum[0].price shouldBe bBrandTopProduct.price
            }
        }

        When("findLowestPriceProductsEachCategories is called") {
            every { productRepository.findProductsWithLowestPrice() } returns
                listOf(aBrandTopProduct, aBrandBottomProduct)

            val result = productService.findLowestPriceProductsEachCategories()

            Then("it should return products with the lowest price for each category") {
                result.products.size shouldBe 2
                result.amount shouldBe aBrandTopProduct.price + aBrandBottomProduct.price
                result.products[0].category shouldBe aBrandTopProduct.category
                result.products[0].brandName shouldBe aBrandTopProduct.brand.name
                result.products[0].price shouldBe aBrandTopProduct.price
                result.products[1].category shouldBe aBrandBottomProduct.category
                result.products[1].brandName shouldBe aBrandBottomProduct.brand.name
                result.products[1].price shouldBe aBrandBottomProduct.price
            }
        }

        When("findLowestPriceProductsWithBrand is called") {
            every { productRepository.findProductsByBrandWithLowestTotalPrice() } returns
                listOf(aBrandTopProduct, aBrandBottomProduct)

            val result = productService.findLowestPriceProductsWithBrand()

            Then("it should return products for the brand with the lowest total price") {
                result.products.size shouldBe 2
                result.brandName shouldBe aBrand.name
                result.products[0].category shouldBe aBrandTopProduct.category
                result.products[0].price shouldBe aBrandTopProduct.price
                result.products[1].category shouldBe aBrandBottomProduct.category
                result.products[1].price shouldBe aBrandBottomProduct.price
                result.amount shouldBe aBrandTopProduct.price + aBrandBottomProduct.price
            }
        }
    }
})
