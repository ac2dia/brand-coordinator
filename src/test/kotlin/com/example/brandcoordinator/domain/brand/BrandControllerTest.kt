package com.example.brandcoordinator.domain.brand

import com.example.brandcoordinator.domain.brand.dto.BrandPatchRequest
import com.example.brandcoordinator.domain.brand.dto.BrandPostRequest
import com.example.brandcoordinator.domain.brand.model.Brand
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
class BrandControllerTest(
    @Autowired
    val mockMvc: MockMvc,
    @Autowired
    val objectMapper: ObjectMapper,
    @Autowired
    val brandRepository: BrandRepository,
) : BehaviorSpec({

    beforeSpec {
        brandRepository.save(Brand(name = "A"))
        brandRepository.save(Brand(name = "B"))
    }

    afterSpec {
        brandRepository.deleteAll()
    }

    Given("a brand API") {

        When("GET /api/v1/brands is called") {
            Then("it should return the list of brands") {
                mockMvc.perform(get("/api/v1/brands"))
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].name").value("A"))
                    .andExpect(jsonPath("$[1].id").value(2L))
                    .andExpect(jsonPath("$[1].name").value("B"))
            }
        }

        When("POST /api/v1/brands is called with invalid request") {
            val brandPostRequest = BrandPostRequest(name = "")

            Then("it shouldn't save the new brand") {
                val requestBody = objectMapper.writeValueAsString(brandPostRequest)

                mockMvc.perform(
                    post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                    .andExpect(status().isBadRequest)
                    .andExpect(jsonPath("$.name").value("name is required"))
            }
        }

        When("POST /api/v1/brands is called with valid request") {
            val brandPostRequest = BrandPostRequest(name = "NewBrand")

            Then("it should save the new brand") {
                val requestBody = objectMapper.writeValueAsString(brandPostRequest)

                mockMvc.perform(
                    post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                    .andExpect(status().isNoContent)
            }
        }

        When("PATCH /api/v1/brands/{id} is called with invalid request") {
            val brandPatchRequest = BrandPatchRequest(name = "")
            val requestBody = objectMapper.writeValueAsString(brandPatchRequest)

            Then("it should update the brand") {
                mockMvc.perform(
                    patch("/api/v1/brands/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                    .andExpect(status().isBadRequest)
                    .andExpect(jsonPath("$.name").value("name is required"))
            }
        }

        When("PATCH /api/v1/brands/{id} is called with valid request") {
            val brandPatchRequest = BrandPatchRequest(name = "UpdatedBrand")
            val requestBody = objectMapper.writeValueAsString(brandPatchRequest)

            Then("it should update the brand") {
                mockMvc.perform(
                    patch("/api/v1/brands/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                    .andExpect(status().isOk)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("UpdatedBrand"))
            }
        }

        When("DELETE /api/v1/brands/{id} is called") {
            Then("it should delete the brand") {
                mockMvc.perform(delete("/api/v1/brands/{id}", 1L))
                    .andExpect(status().isNoContent)
            }
        }
    }
})
