package shop.frankit.domain.product.model.option.impl

import jakarta.persistence.*
import shop.frankit.domain.product.model.ProductOptionAttribute
import shop.frankit.domain.product.model.option.AbstractProductOption

@Entity
@DiscriminatorValue("select")
class ProductSelectOption(

    optionName: String,

    attributeList: List<ProductOptionAttribute>

): AbstractProductOption(name = optionName, attributeList = attributeList.toMutableList()) {

    override fun getMaximumOptionSize(): Int = 5

}