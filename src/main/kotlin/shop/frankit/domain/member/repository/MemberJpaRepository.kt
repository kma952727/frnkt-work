package shop.frankit.domain.member.repository

import shop.frankit.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository: JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
    fun findByEmailAndPassword(email: String, password: String): Member?
}