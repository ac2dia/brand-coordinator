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

    @Query(
        "SELECT p FROM Product p JOIN FETCH p.brand b " +
            "WHERE p.price = (SELECT MIN(p2.price) FROM Product p2 WHERE p2.category = p.category)"
    )
    fun findProductsWithLowestPrice(): List<Product>

    @Query(
        "SELECT p FROM Product p " +
            "JOIN p.brand b " +
            "WHERE b.id = (" +
            "  SELECT b2.id FROM Product p2 " +
            "  JOIN p2.brand b2 " +
            "  GROUP BY b2.id " +
            "  ORDER BY SUM(p2.price) ASC " +
            "  LIMIT 1" +
            ")"
    )
    fun findProductsByBrandWithLowestTotalPrice(): List<Product>
}
