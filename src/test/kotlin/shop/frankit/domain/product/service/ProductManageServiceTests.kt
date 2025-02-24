package shop.frankit.domain.product.service

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import shop.frankit.domain.member.repository.MemberJpaRepository
import shop.frankit.domain.product.exception.NotFoundProductException
import shop.frankit.domain.product.model.Product
import shop.frankit.domain.product.repository.ProductJpaRepository
import shop.frankit.helper.DataBaseCleanUp
import shop.frankit.helper.FixtureGenerator
import java.time.LocalDateTime
import kotlin.test.assertEquals

@SpringBootTest
class ProductManageServiceTests @Autowired constructor(
    private val sut: ProductManageService,
    private val productJpaRepository: ProductJpaRepository,
    private val memberJpaRepository: MemberJpaRepository,
    private val dataBaseCleanUp: DataBaseCleanUp
) {

    @BeforeEach
    fun setUp() {
        dataBaseCleanUp.cleanRdb()
    }

    @Nested
    @DisplayName("상품 1개를 조회할 수 있다.")
    inner class FindProductById {

        @Test
        fun `상품 1개를 조회할 수 있다`() {
            // given
            val owner = memberJpaRepository.save(FixtureGenerator.createMember(email = "JaNa@gmail.com", isSetUpPk = false))
            val product = Product(
                name = "600x500 책상",
                description = "변색 X, 튼튼한, 우드톤 모던 책상",
                basePrice = 43200,
                optionList = mutableListOf(
                    FixtureGenerator.createOption(3, isSetUpPk = false),
                    FixtureGenerator.createOption(2, isSetUpPk = false),
                ),
                owner = owner,
                registeredAt = LocalDateTime.now()
            )
            productJpaRepository.save(product)

            // when
            val result = sut.findProductById(productId = 1)

            // when
            assertAll(
                { assertEquals("600x500 책상", result.name) },
                { assertEquals("변색 X, 튼튼한, 우드톤 모던 책상", result.description) },
                { assertEquals(43200, result.basePrice) },
                { assertEquals(owner.id, result.ownerId) }
            )
        }

        @Test
        fun `삭제된 상품은 조회할 수 없다`() {
            // given
            val owner = memberJpaRepository.save(FixtureGenerator.createMember(email = "JaNa@gmail.com", isSetUpPk = false))
            val product = Product(
                name = "600x500 책상",
                description = "변색 X, 튼튼한, 우드톤 모던 책상",
                basePrice = 43200,
                optionList = mutableListOf(FixtureGenerator.createOption(3, isSetUpPk = false)),
                owner = owner,
                registeredAt = LocalDateTime.now()
            )

            productJpaRepository.save(product)

            product.softDeleteProduct(memberId = 1, now = LocalDateTime.now())
            productJpaRepository.save(product)

            // when & then
            assertThatThrownBy { sut.findProductById(productId = 1) }
                .isInstanceOf(NotFoundProductException::class.java)

        }
    }
}