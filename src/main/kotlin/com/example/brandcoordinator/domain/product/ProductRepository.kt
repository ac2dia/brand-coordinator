package com.example.brandcoordinator.domain.product

import com.example.brandcoordinator.domain.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p JOIN FETCH p.brand")
    override fun findAll(): List<Product>

    fun findByBrandId(brandId: Long): List<Product>

    @Query("SELECT p FROM Product p JOIN FETCH p.brand WHERE p.category = :category")
    fun findByCategory(@Param("category") category: String): List<Product>
}
