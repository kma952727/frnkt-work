package shop.frankit.domain.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import shop.frankit.domain.member.exception.NotFoundMemberException
import shop.frankit.domain.member.model.Member
import shop.frankit.domain.member.repository.MemberJpaRepository

@Service
class MemberQueryBaseService(
    private val memberJpaRepository: MemberJpaRepository
) {

    fun findMemberByIdOrThrow(memberId: Long): Member {
        return memberJpaRepository.findByIdOrNull(memberId)
            ?: throw NotFoundMemberException(condition = "id : $memberId")
    }

    fun findMemberByEmailOrThrow(email: String): Member {
        return memberJpaRepository.findByEmail(email)
            ?: throw NotFoundMemberException(condition = "email : $email")
    }
}