package shop.frankit.domain.product.dto

import io.swagger.v3.oas.annotations.media.Schema
import shop.frankit.domain.product.model.Product

data class FindPageProductResponseDto (

    @Schema(description = "상품 id")
    val id: Long,

    @Schema(description = "상품 이름")
    val name: String,

    @Schema(description = "상품 설명")
    val description: String,

    @Schema(description = "상품 기본 가격")
    val basePrice: Int,

    @Schema(description = "상품 주인")
    val ownerId: Long,

    @Schema(description = "상품의 옵션 목록")
    val optionList: List<ProductOptionResponseDto>
) {
    constructor(product: Product): this(
        id = product.id!!,
        name = product.name,
        description = product.description,
        basePrice = product.basePrice,
        ownerId = product.owner.id!!,
        optionList = product.optionList.map { option ->
            ProductOptionResponseDto(
                id = option.id!!,
                name = option.name,
                attributeList = option.attributeList.map { attribute ->
                    OptionAttributeResponseDto(
                        id = attribute.id!!,
                        name = attribute.name,
                        price = attribute.price
                    )
                }
            )
        }
    )

    data class ProductOptionResponseDto(
        @Schema(description = "상품 옵션 id")
        val id: Long,

        @Schema(description = "상품 옵션 이름")
        val name: String,

        @Schema(description = "상품 옵션의 선택지 목록")
        val attributeList: List<OptionAttributeResponseDto>
    )

    data class OptionAttributeResponseDto(
        @Schema(description = "옵션 선택지 id")
        val id: Long,

        @Schema(description = "옵션 선택지 이름")
        val name: String,

        @Schema(description = "옵션 선택지 가격")
        val price: Int
    )
}