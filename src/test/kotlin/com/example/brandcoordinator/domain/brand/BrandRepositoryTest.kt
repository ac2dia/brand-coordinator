package com.example.brandcoordinator.domain.brand

import com.example.brandcoordinator.domain.brand.model.Brand
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest


@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BrandRepositoryTest(
    @Autowired
    val brandRepository: BrandRepository
): BehaviorSpec({

    beforeEach {
        brandRepository.deleteAll()
    }

    Given("a brand repository") {

        When("findByName is called with an existing brand name") {
            brandRepository.save(Brand(name = "A"))

            Then("it should find the brand by name") {
                val foundBrand = brandRepository.findByName("A")

                foundBrand.isPresent shouldBe true
                foundBrand.get().name shouldBe "A"
            }
        }

        When("findByName is called with a non-existing brand name") {
            Then("it should not find any brand") {
                val foundBrand = brandRepository.findByName("NonExistingBrand")

                foundBrand.isPresent shouldBe false
            }
        }
    }
})
