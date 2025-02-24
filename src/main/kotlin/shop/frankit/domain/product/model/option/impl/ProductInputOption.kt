package shop.frankit.domain.product.model.option.impl

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import shop.frankit.domain.product.exception.TooManyOptionAttributeException
import shop.frankit.domain.product.model.ProductOptionAttribute
import shop.frankit.domain.product.model.option.AbstractProductOption

/**
 * "사용자가 직접 값을 입력할 수 있음"
 */
@Entity
@DiscriminatorValue("input")
class ProductInputOption(

    optionName: String,

    attributeList: List<ProductOptionAttribute>

): AbstractProductOption(name = optionName, attributeList = attributeList.toMutableList()) {

    override fun getMaximumOptionSize(): Int = 1

}