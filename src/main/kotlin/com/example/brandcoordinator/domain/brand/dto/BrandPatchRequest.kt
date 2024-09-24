package com.example.brandcoordinator.domain.brand.dto

import jakarta.validation.constraints.NotBlank

data class BrandPatchRequest(
    @field:NotBlank(message = "name is required")
    val name: String,
)
