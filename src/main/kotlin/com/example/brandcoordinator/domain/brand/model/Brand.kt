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
    @Column(nullable = false)
    var name: String,
) : BaseEntity()
