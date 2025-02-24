package shop.frankit.domain.product.dto.service

import shop.frankit.domain.product.dto.UpdateProductOptionRequestDto

data class UpdateOptionDto (
    val optionName: String,
    val optionAttributeList: List<ProductOptionAttribute>
) {

    constructor(requestDto: UpdateProductOptionRequestDto): this(
        optionName = requestDto.optionName,
        optionAttributeList = requestDto.updateOptionAttributeList
            .map {
                ProductOptionAttribute(
                    attributeId = it.attributeId,
                    name = it.attributeName,
                    price = it.attributePrice
                )
            }
    )

    data class ProductOptionAttribute(
        val attributeId: Long,
        val name: String,
        val price: Int
    )

}