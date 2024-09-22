package com.example.brandcoordinator.domain.product

import com.example.brandcoordinator.domain.brand.BrandRepository
import com.example.brandcoordinator.domain.brand.model.Brand
import com.example.brandcoordinator.domain.product.model.Product
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ProductRepositoryTest(
    @Autowired private val productRepository: ProductRepository,
    @Autowired private val brandRepository: BrandRepository
) : BehaviorSpec({

    beforeEach {
        productRepository.deleteAll()
        brandRepository.deleteAll()
    }

    Given("a ProductRepository and BrandRepository") {

        When("findAll is called") {
            val aBrand = brandRepository.save(Brand(name = "A"))
            val bBrand = brandRepository.save(Brand(name = "B"))

            productRepository.save(Product(category = "상의", brand = aBrand, price = 4000))
            productRepository.save(Product(category = "상의", brand = bBrand, price = 6000))
            productRepository.save(Product(category = "하의", brand = aBrand, price = 10000))
            productRepository.save(Product(category = "하의", brand = bBrand, price = 11000))

            val products = productRepository.findAll()

            Then("it should return all products with their brand fetched") {
                products.size shouldBe 4
            }
        }

        When("findByBrandId is called with a valid brandId") {
            val aBrand = brandRepository.save(Brand(name = "A"))
            productRepository.save(Product(category = "상의", brand = aBrand, price = 4000))
            productRepository.save(Product(category = "하의", brand = aBrand, price = 10000))

            val productsByBrandA = productRepository.findByBrandId(aBrand.id)

            Then("it should return all products for that brand") {
                productsByBrandA.size shouldBe 2
                productsByBrandA[0].brand.name shouldBe aBrand.name
            }
        }

        When("findByCategory is called with a valid category") {
            val aBrand = brandRepository.save(Brand(name = "A"))
            val bBrand = brandRepository.save(Brand(name = "B"))

            val aBrandTopProduct = productRepository.save(Product(category = "상의", brand = aBrand, price = 4000))
            val bBrandTopProduct = productRepository.save(Product(category = "상의", brand = bBrand, price = 6000))

            val productsByCategory = productRepository.findByCategory("상의")

            Then("it should return all products for that category") {
                productsByCategory.size shouldBe 2
                productsByCategory[0].category shouldBe aBrandTopProduct.category
                productsByCategory[1].category shouldBe bBrandTopProduct.category
            }
        }

        When("findProductsWithLowestPrice is called") {
            val aBrand = brandRepository.save(Brand(name = "A"))
            val bBrand = brandRepository.save(Brand(name = "B"))

            productRepository.save(Product(category = "상의", brand = aBrand, price = 4000))
            productRepository.save(Product(category = "하의", brand = aBrand, price = 20000))
            productRepository.save(Product(category = "상의", brand = bBrand, price = 6000))
            productRepository.save(Product(category = "하의", brand = bBrand, price = 11000))

            val lowestPriceProducts = productRepository.findProductsWithLowestPrice()

            Then("it should return the products with the lowest price per category") {
                lowestPriceProducts.size shouldBe 2
                lowestPriceProducts[0].category shouldBe "상의"
                lowestPriceProducts[0].brand.name shouldBe aBrand.name
                lowestPriceProducts[0].price shouldBe 4000
                lowestPriceProducts[1].category shouldBe "하의"
                lowestPriceProducts[1].brand.name shouldBe bBrand.name
                lowestPriceProducts[1].price shouldBe 11000
            }
        }

        When("findProductsByBrandWithLowestTotalPrice is called") {
            val aBrand = brandRepository.save(Brand(name = "A"))
            val bBrand = brandRepository.save(Brand(name = "B"))

            productRepository.save(Product(category = "상의", brand = aBrand, price = 4000))
            productRepository.save(Product(category = "하의", brand = aBrand, price = 10000))
            productRepository.save(Product(category = "상의", brand = bBrand, price = 6000))
            productRepository.save(Product(category = "하의", brand = bBrand, price = 11000))

            val lowestTotalPriceBrandProducts = productRepository.findProductsByBrandWithLowestTotalPrice()

            Then("it should return products for the brand with the lowest total price") {
                lowestTotalPriceBrandProducts.size shouldBe 2
                lowestTotalPriceBrandProducts[0].category shouldBe "상의"
                lowestTotalPriceBrandProducts[0].brand.name shouldBe aBrand.name
                lowestTotalPriceBrandProducts[0].price shouldBe 4000
                lowestTotalPriceBrandProducts[1].category shouldBe "하의"
                lowestTotalPriceBrandProducts[0].brand.name shouldBe aBrand.name
                lowestTotalPriceBrandProducts[1].price shouldBe 10000
            }
        }
    }
})
