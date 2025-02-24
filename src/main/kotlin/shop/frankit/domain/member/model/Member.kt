package shop.frankit.domain.member.model

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import shop.frankit.common.jpa.BaseEntity

@Entity
@Table(name = "member")
class Member (

    @Comment(value = "사용자 이메일")
    @Column(name = "email", unique = true)
    val email: String,

    @Comment(value = "사용자 암호")
    @Column(name = "password")
    val password: String

): BaseEntity()