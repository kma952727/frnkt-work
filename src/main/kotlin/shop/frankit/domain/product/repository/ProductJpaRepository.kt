package shop.frankit.domain.product.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH
import org.springframework.data.jpa.repository.JpaRepository
import shop.frankit.domain.product.model.Product

interface ProductJpaRepository: JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = ["optionList", "optionList.attributeList" ], type = FETCH)
    fun findWithOptionsAndAttributesById(id: Long): Product?
}