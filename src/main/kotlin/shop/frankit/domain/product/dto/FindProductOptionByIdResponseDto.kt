package shop.frankit.domain.product.dto

import shop.frankit.domain.product.model.option.AbstractProductOption

data class FindProductOptionByIdResponseDto (
    val name: String,
    val attributeList: List<ProductOptionAttributeResponseDto>
) {
    constructor(productOption: AbstractProductOption): this(
        name = productOption.name,
        attributeList = productOption.attributeList
            .map { ProductOptionAttributeResponseDto(name = it.name, price = it.price) }
    )

    data class ProductOptionAttributeResponseDto(
        val name: String,
        val price: Int
    )
}