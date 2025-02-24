package shop.frankit.domain.product.dto.service

import shop.frankit.domain.product.dto.AddProductOptionRequestDto
import shop.frankit.domain.product.model.vo.ProductOptionType

data class AddOptionDto (
    val optionName: String,
    val optionType: ProductOptionType,
    val optionAttributeList: List<ProductOptionAttribute>
) {

    constructor(requestDto: AddProductOptionRequestDto): this(
        optionName = requestDto.optionName,
        optionType = requestDto.optionType,
        optionAttributeList = requestDto.optionAttributeList.map { ProductOptionAttribute(name = it.attributeName, price = it.price) }
    )

    data class ProductOptionAttribute (
        val name: String,
        val price: Int
    )

}