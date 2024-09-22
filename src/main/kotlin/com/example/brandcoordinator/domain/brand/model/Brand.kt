package com.example.brandcoordinator.domain.brand.model

import com.example.brandcoordinator.domain.brand.dto.BrandPostRequest
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Brand(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false, unique = true)
    var name: String,
) {
    companion object {
        fun from(brandPostRequest: BrandPostRequest): Brand =
            Brand(
                name = brandPostRequest.name,
            )
    }

    fun update(name: String) {
        this.name = name
    }
}
