package shop.frankit.domain.product.model

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import shop.frankit.domain.product.dto.service.UpdateOptionDto
import shop.frankit.domain.product.exception.ConflictOptionAttributeNameException
import shop.frankit.domain.product.exception.NotFoundOptionAttributeException
import shop.frankit.domain.product.exception.TooManyOptionAttributeException
import shop.frankit.domain.product.model.option.impl.ProductInputOption
import shop.frankit.domain.product.model.option.impl.ProductSelectOption
import shop.frankit.helper.EntityIdInjector
import java.time.LocalDateTime

class ProductOptionTests {
    @Nested
    @DisplayName("옵션 선택지를 추가할 수 있다.")
    inner class AddOptionAttribute {
        @Test
        fun `1개를 추가할 수 있다`() {
            // given
            val sut = ProductSelectOption(optionName = "건전지 팩", attributeList = listOf())

            // when
            sut.addOptionAttribute(attributeName = "3500mha", attributePrice = 3000)

            // then
            assertEquals(sut.attributeList[0].price, 3000)
        }

        @Test
        fun `4개를 추가할 수 있다`() {
            // given & when
            val sut = ProductSelectOption(optionName = "건전지 팩", attributeList = listOf())
                .apply {
                    addOptionAttribute(attributeName = "기본형", attributePrice = 0)
                    addOptionAttribute(attributeName = "3500mha", attributePrice = 1000)
                    addOptionAttribute(attributeName = "6500mha", attributePrice = 3000)
                    addOptionAttribute(attributeName = "7500mha + 실리콘 케이스", attributePrice = 5000)
                }

            // then
            assertAll(
                { assertEquals(4, sut.attributeList.size) }
            )
        }

        @Test
        fun `선택 옵션은 최대 5개의 선택지를 가질 수 있다`() {
            // given
            val sut = ProductSelectOption(optionName = "건전지 팩", attributeList = listOf())
                .apply {
                    addOptionAttribute(attributeName = "기본형", attributePrice = 0)
                    addOptionAttribute(attributeName = "3500mha", attributePrice = 1000)
                    addOptionAttribute(attributeName = "4500mha", attributePrice = 2000)
                    addOptionAttribute(attributeName = "5500mha", attributePrice = 3000)
                    addOptionAttribute(attributeName = "6500mha", attributePrice = 4000)
                }

            // when & then
            assertThatThrownBy{ sut.addOptionAttribute(attributeName = "7500mha + 실리콘 케이스", attributePrice = 5000) }
                .isInstanceOf(TooManyOptionAttributeException::class.java)
        }

        @Test
        fun `입력 옵션은 1개의 선택지만 가질 수 있다`() {
            // given
            val sut = ProductInputOption(optionName = "건전지 팩", attributeList = listOf())
                .apply {
                    addOptionAttribute(attributeName = "기본형", attributePrice = 0)
                }

            // when & then
            assertThatThrownBy{ sut.addOptionAttribute(attributeName = "7500mha + 실리콘 케이스", attributePrice = 5000) }
                .isInstanceOf(TooManyOptionAttributeException::class.java)
        }

        @Test
        fun `중복된 이름을 가진 선택지를 추가할 수 없다`() {
            // given
            val sut = ProductSelectOption(optionName = "건전지 팩", attributeList = listOf())
                .apply {
                    addOptionAttribute(attributeName = "기본형", attributePrice = 0)
                }

            // when & then
            assertThatThrownBy{ sut.addOptionAttribute(attributeName = "기본형", attributePrice = 5000) }
                .isInstanceOf(ConflictOptionAttributeNameException::class.java)
        }
    }

    @Nested
    @DisplayName("옵션을 삭제할 수 있다.")
    inner class SoftDeleteOption {
        @Test
        fun `옵션을 삭제하면 내부 선택자도 함께 삭제된다`() {
            // given
            val sut = ProductSelectOption(optionName = "건전지 팩", attributeList = listOf())
                .apply {
                    addOptionAttribute(attributeName = "기본형", attributePrice = 0)
                    addOptionAttribute(attributeName = "3500mha", attributePrice = 1000)
                    addOptionAttribute(attributeName = "6500mha", attributePrice = 3000)
                    addOptionAttribute(attributeName = "7500mha", attributePrice = 4000)
                }
            // when
            sut.softDeleteOption(now = LocalDateTime.now())

            // then
            assertAll(
                { assertEquals(4, sut.attributeList.size) },
                { assertTrue(sut.attributeList.map { it.isDeleted }.all { true }) },
            )
        }
    }

    @Nested
    @DisplayName("옵션을 수정할 수 있다.")
    inner class UpdateOption {
        @Test
        fun `옵션 선택지 목록을 수정할 수 있다`() {
            // given
            val sut = ProductSelectOption(optionName = "건전지 팩", attributeList = listOf())
                .apply {
                    addOptionAttribute(attributeName = "기본형", attributePrice = 0)
                    addOptionAttribute(attributeName = "3500mha", attributePrice = 3000)
                }
            EntityIdInjector.inject(entity = sut.attributeList[0], id = 1)
            EntityIdInjector.inject(entity = sut.attributeList[1], id = 2)

            val option = UpdateOptionDto(
                optionName = "건전지 팩 [할인]",
                optionAttributeList = listOf(
                    UpdateOptionDto.ProductOptionAttribute(attributeId = 2, name = "3500mha", price = 2400)
                )
            )

            // when
            sut.update(updateOption = option, now = LocalDateTime.now())

            // then
            assertAll(
                { assertEquals("건전지 팩 [할인]", sut.name) },
                { assertEquals(2400, sut.attributeList[1].price) }
            )
        }

        @Test
        fun `존재하지 않는 선택지는 수정할 수 없다`() {
            // given
            val sut = ProductSelectOption(optionName = "건전지 팩", attributeList = listOf())
                .apply {
                    addOptionAttribute(attributeName = "기본형", attributePrice = 0)
                }
            val option = UpdateOptionDto(
                optionName = "건전지 팩 [할인]",
                optionAttributeList = listOf(
                    UpdateOptionDto.ProductOptionAttribute(attributeId = 30, name = "3500mha", price = 2400)
                )
            )

            // when & then
            assertThatThrownBy { sut.update(updateOption = option, now = LocalDateTime.now()) }
                .isInstanceOf(NotFoundOptionAttributeException::class.java)
        }
    }
}