package shop.frankit.domain.product.model

import jakarta.persistence.*
import jakarta.persistence.CascadeType.PERSIST
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.Comment
import org.hibernate.annotations.SQLRestriction
import shop.frankit.common.jpa.BaseEntity
import shop.frankit.domain.member.model.Member
import shop.frankit.domain.product.dto.service.AddOptionDto
import shop.frankit.domain.product.exception.ConflictProductOptionNameException
import shop.frankit.domain.product.exception.NotMatchProductAndOptionException
import shop.frankit.domain.product.exception.NotProductOwnerException
import shop.frankit.domain.product.exception.TooManyProductOptionException
import shop.frankit.domain.product.model.option.AbstractProductOption
import java.time.LocalDateTime

@Entity
@SQLRestriction("is_deleted = false")
@Table(name = "product")
class Product (

    @Comment("상품 이름")
    @Column(name = "name")
    var name: String,

    @Comment("상품 설명")
    @Column(name = "description")
    var description: String,

    @Comment("상품 기본 가격")
    @Column(name = "base_price")
    var basePrice: Int,

    @Comment("상품이 가지고 있는 옵션")
    @OneToMany(cascade = [PERSIST])
    @JoinColumn(name = "product_id")
    @BatchSize(size=100)
    val optionList: MutableList<AbstractProductOption> = mutableListOf(),

    @Comment("상품을 올린 판매자")
    @ManyToOne
    @JoinColumn(name = "member_id")
    val owner: Member,

    @Comment("등록 일시")
    @Column(name = "registered_at")
    val registeredAt: LocalDateTime = LocalDateTime.now()

): BaseEntity() {

    fun update(newName: String, newDescription: String, newBasePrice: Int, memberId: Long, now: LocalDateTime) {
        if(owner.id!! != memberId) {
            throw NotProductOwnerException(productId = id!!, requestMemberId = memberId)
        }

        name = newName
        description = newDescription
        basePrice = newBasePrice
    }

    fun softDeleteProduct(memberId: Long, now: LocalDateTime) {
        if(owner.id!! != memberId) {
            throw NotProductOwnerException(productId = id!!, requestMemberId = memberId)
        }

        softDeleteBaseEntity(now)
        optionList.forEach{ option ->
            option.softDeleteOption(now = now)
            option.attributeList.forEach { attribute -> attribute.softDeleteBaseEntity(now = now) }
        }
    }

    fun addOption(addOptionDto: AddOptionDto) {
        val maximumSize = 5
        if(optionList.size >= maximumSize) {
            throw TooManyProductOptionException(maximumSize = maximumSize)
        }
        if(optionList.map { it.name }.contains(addOptionDto.optionName)) {
            throw ConflictProductOptionNameException(optionName = addOptionDto.optionName)
        }

        val option = AbstractProductOption.create(
            type = addOptionDto.optionType,
            optionName = addOptionDto.optionName,
            attributeList = addOptionDto.optionAttributeList.map { ProductOptionAttribute(name = it.name, price = it.price) }
        )

        optionList.add(option)
    }

    fun softDeleteOption(optionId: Long, memberId: Long, now: LocalDateTime) {
        if(owner.id != memberId) {
            throw NotProductOwnerException(productId = id!!, requestMemberId = memberId)
        }

        optionList.find { it.id == optionId }
            ?.let { option ->
                option.softDeleteOption(now = now)
                optionList.remove(option)
            }
            ?: throw NotMatchProductAndOptionException(optionId = optionId)
    }

}