package shop.frankit.helper

import shop.frankit.domain.member.model.Member
import shop.frankit.domain.product.model.ProductOptionAttribute
import shop.frankit.domain.product.model.option.impl.ProductInputOption

class FixtureGenerator {
    companion object {
        fun createOption(attributeSize: Int = 1, optionId: Long = 1, isSetUpPk: Boolean = true): ProductInputOption {
            val option = ProductInputOption(
                optionName = "이름",
                attributeList = List(attributeSize + 1) { ProductOptionAttribute(name = "옵션속성 $it", price = 100 * it) }
            )
            if(isSetUpPk) {
                EntityIdInjector.inject(entity = option, id = optionId)
            }
            return option
        }

        fun createMember(memberId: Long = 1, email: String = "jamei12@gmail.com", isSetUpPk: Boolean = true): Member {
            val member = Member(
                email = email,
                password = "JaJAJAJf323g"
            )
            if(isSetUpPk) {
                EntityIdInjector.inject(entity = member, id = memberId)
            }

            return member
        }
    }
}