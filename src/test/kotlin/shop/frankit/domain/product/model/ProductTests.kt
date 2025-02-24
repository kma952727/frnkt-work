package shop.frankit.domain.product.model

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import shop.frankit.domain.product.dto.service.AddOptionDto
import shop.frankit.domain.product.exception.ConflictProductOptionNameException
import shop.frankit.domain.product.exception.NotProductOwnerException
import shop.frankit.domain.product.exception.TooManyProductOptionException
import shop.frankit.domain.product.model.vo.ProductOptionType.SELECT
import shop.frankit.helper.EntityIdInjector
import shop.frankit.helper.FixtureGenerator
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductTests {

    @Nested
    @DisplayName("상품을 업데이트할 수 있다.")
    inner class Update {
        @Test
        fun `상품을 업데이트 할 수 있다`() {
            // given
            val sut = Product(
                name = "600x500 책상",
                description = "변색 X, 튼튼한, 우드톤 모던 책상",
                basePrice = 43200,
                optionList = mutableListOf(FixtureGenerator.createOption(3)),
                owner = FixtureGenerator.createMember(),
                registeredAt = LocalDateTime.now()
            )
            EntityIdInjector.inject(entity = sut, id = 1)

            // when
            sut.update(
                newName = "600x550 책상 [HOT]",
                newDescription = "",
                newBasePrice = 43100,
                memberId = 1,
                now = LocalDateTime.now()
            )

            // then
            assertAll(
                { assertEquals("600x550 책상 [HOT]", sut.name) },
                { assertEquals("", sut.description) },
                { assertEquals(43100, sut.basePrice) },
                { assertEquals(1, sut.owner.id) }
            )
        }

        @Test
        fun `자신의 상품만 업데이트할 수 있다`() {
            // given
            val sut = Product(
                name = "600x500 책상",
                description = "변색 X, 튼튼한, 우드톤 모던 책상",
                basePrice = 43200,
                optionList = mutableListOf(FixtureGenerator.createOption(3)),
                owner = FixtureGenerator.createMember(),
                registeredAt = LocalDateTime.now()
            )
            EntityIdInjector.inject(entity = sut, id = 1)

            // when & then
            assertThatThrownBy {
                sut.update(
                    newName = "600x550 책상 [HOT]",
                    newDescription = "",
                    newBasePrice = 43100,
                    memberId = 2,
                    now = LocalDateTime.now()
                )
            }.isInstanceOf(NotProductOwnerException::class.java)
        }
    }

    @Nested
    @DisplayName("상품의 옵션을 추가할수 있다.")
    inner class AddOption {

        @Test
        fun `옵션을 2개 추가할 수 있다`() {
            // given
            val sut = Product(
                name = "600x500 책상",
                description = "변색 X, 튼튼한, 우드톤 모던 책상",
                basePrice = 43200,
                optionList = mutableListOf(FixtureGenerator.createOption(1)),
                owner = FixtureGenerator.createMember(),
                registeredAt = LocalDateTime.now()
            )
            EntityIdInjector.inject(entity = sut, id = 1)

            val addOptionDto = AddOptionDto(
                optionName = "코팅",
                optionType = SELECT,
                optionAttributeList = listOf(
                    AddOptionDto.ProductOptionAttribute(name = "긁힘 코팅", price = 9500),
                    AddOptionDto.ProductOptionAttribute(name = "방수 코팅", price = 11500),
                )
            )

            // when
            sut.addOption(addOptionDto = addOptionDto)

            // then
            assertAll(
                { assertEquals(2, sut.optionList.size) },
                { assertEquals("긁힘 코팅", sut.optionList[1].attributeList[0].name) },
                { assertEquals(9500, sut.optionList[1].attributeList[0].price) },
                { assertEquals("방수 코팅", sut.optionList[1].attributeList[1].name) },
                { assertEquals(11500, sut.optionList[1].attributeList[1].price) },
            )
        }

        @Test
        fun `옵션은 최대 5개까지 추가할 수 있다`() {
            // given
            val sut = Product(
                name = "600x500 책상",
                description = "변색 X, 튼튼한, 우드톤 모던 책상",
                basePrice = 43200,
                optionList = mutableListOf(
                    FixtureGenerator.createOption(attributeSize = 1),
                    FixtureGenerator.createOption(attributeSize = 1),
                    FixtureGenerator.createOption(attributeSize = 1),
                    FixtureGenerator.createOption(attributeSize = 1),
                    FixtureGenerator.createOption(attributeSize = 1),
                ),
                owner = FixtureGenerator.createMember(),
                registeredAt = LocalDateTime.now()
            )
            EntityIdInjector.inject(entity = sut, id = 1)

            val addOptionDto = AddOptionDto(
                optionName = "코팅",
                optionType = SELECT,
                optionAttributeList = listOf(
                    AddOptionDto.ProductOptionAttribute(name = "긁힘 코팅", price = 9500),
                    AddOptionDto.ProductOptionAttribute(name = "방수 코팅", price = 11500),
                )
            )

            // when & then
            assertThatThrownBy { sut.addOption(addOptionDto = addOptionDto) }
                .isInstanceOf(TooManyProductOptionException::class.java)
        }

        @Test
        fun `중복된 이름을 가진 옵션은 추가할 수 없다`() {
            // given
            val sut = Product(
                name = "600x500 책상",
                description = "변색 X, 튼튼한, 우드톤 모던 책상",
                basePrice = 43200,
                optionList = mutableListOf(FixtureGenerator.createOption(attributeSize = 1)),
                owner = FixtureGenerator.createMember(),
                registeredAt = LocalDateTime.now()
            )
                .apply { optionList[0].name = "모니터 받침대" }
            EntityIdInjector.inject(entity = sut, id = 1)

            val addOptionDto = AddOptionDto(
                optionName = "모니터 받침대",
                optionType = SELECT,
                optionAttributeList = listOf(AddOptionDto.ProductOptionAttribute(name = "철제 받침대", price = 9500))
            )

            // when & then
            assertThatThrownBy { sut.addOption(addOptionDto = addOptionDto) }
                .isInstanceOf(ConflictProductOptionNameException::class.java)
        }
    }

    @Nested
    @DisplayName("상품을 제거할 수 있다.")
    inner class SoftDeleteProduct {
        @Test
        fun `상품 1개를 제거할 수 있다`() {
            // given
            val sut = Product(
                name = "600x500 책상",
                description = "변색 X, 튼튼한, 우드톤 모던 책상",
                basePrice = 43200,
                optionList = mutableListOf(
                    FixtureGenerator.createOption(attributeSize = 1, optionId = 1),
                    FixtureGenerator.createOption(attributeSize = 1, optionId = 2)
                ),
                owner = FixtureGenerator.createMember(memberId = 1),
                registeredAt = LocalDateTime.now()
            )

            // when
            sut.softDeleteProduct(memberId = 1, now = LocalDateTime.now())

            // then
            val isDeletedOptionList = sut.optionList.map { it.isDeleted }.all { true }
            val isDeletedAttributeList = sut.optionList.flatMap { option ->
                option.attributeList.map { attribute ->
                    attribute.isDeleted
                }
            }.all { true }

            assertAll(
                { assertTrue(isDeletedOptionList) },
                { assertTrue(isDeletedAttributeList) }
            )
        }
    }

    @Nested
    @DisplayName("상품에 할당된 옵션을 제거할 수 있다.")
    inner class SoftDeleteOption {
        @Test
        fun `옵션 1개를 제거할 수 있다`() {
            // given
            val sut = Product(
                name = "600x500 책상",
                description = "변색 X, 튼튼한, 우드톤 모던 책상",
                basePrice = 43200,
                optionList = mutableListOf(
                    FixtureGenerator.createOption(attributeSize = 1, optionId = 1),
                    FixtureGenerator.createOption(attributeSize = 1, optionId = 2)
                ),
                owner = FixtureGenerator.createMember(),
                registeredAt = LocalDateTime.now()
            )
            EntityIdInjector.inject(entity = sut, id  = 1)

            // when
            sut.softDeleteOption(optionId = 1, memberId = 1, now = LocalDateTime.now())

            // then
            assertAll(
                { assertTrue(sut.optionList.size == 1) }
            )
        }

        @Test
        fun `자신의 상품의 옵션만 제거할 수 있다`() {
            // given
            val sut = Product(
                name = "600x500 책상",
                description = "변색 X, 튼튼한, 우드톤 모던 책상",
                basePrice = 43200,
                optionList = mutableListOf(
                    FixtureGenerator.createOption(attributeSize = 1, optionId = 1),
                    FixtureGenerator.createOption(attributeSize = 1, optionId = 2)
                ),
                owner = FixtureGenerator.createMember(),
                registeredAt = LocalDateTime.now()
            )
            EntityIdInjector.inject(entity = sut, id  = 1)

            // when & then
            assertThatThrownBy { sut.softDeleteOption(optionId = 1, memberId = 15, now = LocalDateTime.now()) }
                .isInstanceOf(NotProductOwnerException::class.java)
        }
    }
}