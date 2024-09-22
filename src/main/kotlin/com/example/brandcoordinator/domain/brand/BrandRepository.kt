package com.example.brandcoordinator.domain.brand

import com.example.brandcoordinator.domain.brand.model.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BrandRepository : JpaRepository<Brand, Long> {
    fun findByName(name: String): Optional<Brand>
}
