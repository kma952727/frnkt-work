package shop.frankit.domain.product.dto

import io.swagger.v3.oas.annotations.media.Schema
import shop.frankit.domain.product.model.Product

class FindProductByIdResponseDto (

    @Schema(description = "상품 이름")
    val name: String,

    @Schema(description = "상품 설명")
    val description: String,

    @Schema(description = "상품 기본 가격")
    val basePrice: Int,

    @Schema(description = "상품의 주인")
    val ownerId: Long
) {
    constructor(product: Product): this(
        name = product.name,
        description = product.description,
        basePrice = product.basePrice,
        ownerId = product.owner.id!!
    )
}