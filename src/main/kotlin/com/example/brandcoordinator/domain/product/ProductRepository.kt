package com.example.brandcoordinator.domain.product

import com.example.brandcoordinator.domain.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
}
