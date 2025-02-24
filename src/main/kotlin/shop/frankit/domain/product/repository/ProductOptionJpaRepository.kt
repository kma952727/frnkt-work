package shop.frankit.domain.product.repository

import org.springframework.data.jpa.repository.JpaRepository
import shop.frankit.domain.product.model.option.AbstractProductOption

interface ProductOptionJpaRepository: JpaRepository<AbstractProductOption, Long>