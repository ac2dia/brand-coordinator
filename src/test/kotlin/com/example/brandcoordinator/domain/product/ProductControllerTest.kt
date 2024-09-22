package com.example.brandcoordinator.domain.product

import com.example.brandcoordinator.domain.brand.BrandRepository
import com.example.brandcoordinator.domain.brand.model.Brand
import com.example.brandcoordinator.domain.product.dto.ProductPatchRequest
import com.example.brandcoordinator.domain.product.dto.ProductPostRequest
import com.example.brandcoordinator.domain.product.model.Product
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest(
    @Autowired
    val mockMvc: MockMvc,
    @Autowired
    val objectMapper: ObjectMapper,
    @Autowired
    val productRepository: ProductRepository,
    @Autowired
    val brandRepository: BrandRepository
) : BehaviorSpec({

    beforeSpec {
        val aBrand = brandRepository.save(Brand(id = 1L, name = "A"))
        productRepository.save(Product(id = 1L, category = "상의", brand = aBrand, price = 4000))
        productRepository.save(Product(id = 2L, category = "하의", brand = aBrand, price = 14000))

        val bBrand = brandRepository.save(Brand(id = 2, name = "B"))
        productRepository.save(Product(id = 3L, category = "상의", brand = bBrand, price = 6000))
        productRepository.save(Product(id = 4L, category = "하의", brand = bBrand, price = 11000))
    }

    afterSpec {
        productRepository.deleteAll()
        brandRepository.deleteAll()
    }

    Given("a product API") {

        When("GET /api/v1/products is called") {
            Then("it should return the list of products") {
                mockMvc.perform(get("/api/v1/products"))
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(4))
            }
        }

        When("POST /api/v1/products is called with valid request") {
            val productPostRequest = ProductPostRequest(category = "신발", brandName = "A", price = 5000)

            Then("it should save the new product") {
                val requestBody = objectMapper.writeValueAsString(productPostRequest)

                mockMvc.perform(
                    post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                    .andExpect(status().isNoContent)
            }
        }

        When("PATCH /api/v1/products/{id} is called with valid request") {
            val productPatchRequest = ProductPatchRequest(category = null, brandName = null, price = 4500)
            val requestBody = objectMapper.writeValueAsString(productPatchRequest)

            Then("it should update the product") {
                mockMvc.perform(
                    patch("/api/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.price").value(4500))
            }
        }

        When("DELETE /api/v1/products/{id} is called") {
            Then("it should delete the product") {
                mockMvc.perform(delete("/api/v1/products/{id}", 5L))
                    .andExpect(status().isNoContent)
            }
        }

        When("GET /api/v1/get-summary-by-category/{category} is called") {
            Then("it should return the summary of the products in the category") {
                mockMvc.perform(get("/api/v1/get-summary-by-category/{category}", "하의"))
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.category").value("하의"))
                    .andExpect(jsonPath("$.minimum[0].brandName").value("B"))
                    .andExpect(jsonPath("$.minimum[0].price").value("11000"))
                    .andExpect(jsonPath("$.maximum[0].brandName").value("A"))
                    .andExpect(jsonPath("$.maximum[0].price").value("14000"))
            }
        }

        When("GET /api/v1/get-summary-each-category is called") {
            Then("it should return the summary for each category") {
                mockMvc.perform(get("/api/v1/get-summary-each-category"))
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.amount").value(15500))
                    .andExpect(jsonPath("$.products[0].category").value("상의"))
                    .andExpect(jsonPath("$.products[0].brandName").value("A"))
                    .andExpect(jsonPath("$.products[0].price").value(4500))
                    .andExpect(jsonPath("$.products[1].category").value("하의"))
                    .andExpect(jsonPath("$.products[1].brandName").value("B"))
                    .andExpect(jsonPath("$.products[1].price").value(11000))
            }
        }

        When("GET /api/v1/get-summary-lowest-brand is called") {
            Then("it should return the brand with the lowest total price across all categories") {
                mockMvc.perform(get("/api/v1/get-summary-lowest-brand"))
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.brandName").value("B"))
                    .andExpect(jsonPath("$.products[0].category").value("상의"))
                    .andExpect(jsonPath("$.products[0].price").value(6000))
                    .andExpect(jsonPath("$.products[1].category").value("하의"))
                    .andExpect(jsonPath("$.products[1].price").value(11000))
                    .andExpect(jsonPath("$.amount").value(17000))
            }
        }
    }
})
