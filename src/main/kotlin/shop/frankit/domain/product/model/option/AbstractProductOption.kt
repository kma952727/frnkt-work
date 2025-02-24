package shop.frankit.domain.product.model.option

import jakarta.persistence.*
import jakarta.persistence.CascadeType.PERSIST
import jakarta.persistence.InheritanceType.SINGLE_TABLE
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.Comment
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import shop.frankit.common.jpa.BaseEntity
import shop.frankit.domain.product.dto.service.UpdateOptionDto
import shop.frankit.domain.product.exception.*
import shop.frankit.domain.product.model.ProductOptionAttribute
import shop.frankit.domain.product.model.option.impl.ProductInputOption
import shop.frankit.domain.product.model.option.impl.ProductSelectOption
import shop.frankit.domain.product.model.vo.ProductOptionType
import shop.frankit.domain.product.model.vo.ProductOptionType.INPUT
import shop.frankit.domain.product.model.vo.ProductOptionType.SELECT
import java.time.LocalDateTime

@Entity
@Table(name = "product_option")
@Inheritance(strategy = SINGLE_TABLE)
@SQLRestriction("is_deleted = false")
@DiscriminatorColumn(name = "option_type")
abstract class AbstractProductOption(

    @Column(name = "옵션 이름")
    var name: String,

    @OneToMany(cascade = [PERSIST])
    @BatchSize(size = 100)
    @Comment("옵션이 포함하는 선택지")
    @JoinColumn(name = "option_id")
    val attributeList: MutableList<ProductOptionAttribute> = mutableListOf()

): BaseEntity() {

    /**
     * 옵션 구현체는 해당 함수에서 최대 가질 수 있는 선택지의 갯수를 반환해야 합니다.
     * ex) 선택 옵션은 최대 5개의 선택지를 가질 수 있다.
     * ex) 입력 옵션은 1개의 선택지만 가질 수 있다.
     *
     * @return 옵션이 가지는 선택지 최대 갯수
     */
    abstract fun getMaximumOptionSize(): Int

    /**
     * 옵션 선택지가 최대 개수를 넘는지 검증합니다.
     * @return 검증을 통과한 옵션 객체(this)
     */
    fun verifyOptionAttributeSize(): AbstractProductOption {
        val maximumSize = getMaximumOptionSize()
        if(attributeList.size >= maximumSize) {
            throw TooManyOptionAttributeException(maximumSize = maximumSize)
        }

        return this
    }

    fun addOptionAttribute(attributeName: String, attributePrice: Int) {
        verifyOptionAttributeSize()
        if(attributeList.map { it.name }.contains(attributeName)) {
            throw ConflictOptionAttributeNameException(attributeName = attributeName)
        }

        attributeList.add(ProductOptionAttribute(name = attributeName, price = attributePrice))
    }

    fun softDeleteOption(now: LocalDateTime) {
        attributeList.forEach { it.softDeleteBaseEntity(now = now) }
        softDeleteBaseEntity(now = now)
//        attributeList.clear()
    }

    fun update(updateOption: UpdateOptionDto, now: LocalDateTime) {
        val attributeMap = attributeList.associateBy { it.id }

        updateOption.optionAttributeList.forEach { attributeDto ->
            attributeMap[attributeDto.attributeId]?.let { attribute ->
                attribute.name = attributeDto.name
                attribute.price = attributeDto.price
            } ?: throw NotFoundOptionAttributeException(attributeId = attributeDto.attributeId)
        }

        name = updateOption.optionName
    }

    companion object {
        fun create(
            optionName: String,
            type: ProductOptionType,
            attributeList: List<ProductOptionAttribute>
        ): AbstractProductOption {
            if(attributeList.isEmpty()) {
                throw OptionAttributeMissingException(minimumOptionSize = 1)
            }

            val option = when(type) {
                INPUT -> ProductInputOption(optionName = optionName, attributeList = attributeList)
                SELECT -> ProductSelectOption(optionName = optionName, attributeList = attributeList)
            }

            return option.verifyOptionAttributeSize()
        }

    }
}