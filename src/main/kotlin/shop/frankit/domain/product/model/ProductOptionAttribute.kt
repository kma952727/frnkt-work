package shop.frankit.domain.product.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import org.hibernate.annotations.SQLRestriction
import shop.frankit.common.jpa.BaseEntity

@Entity
@SQLRestriction("is_deleted = false")
@Table(name = "product_option_attribute")
class ProductOptionAttribute (

    @Comment("선택지 이름")
    @Column(name = "name")
    var name: String,

    @Comment("선택지 가격")
    @Column(name = "price")
    var price: Int

): BaseEntity()