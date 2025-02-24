package shop.frankit.domain.member.service

import shop.frankit.common.component.JwtComponent
import shop.frankit.domain.member.dto.LoginRequestDto
import shop.frankit.domain.member.dto.LoginResponseDto
import shop.frankit.domain.member.dto.SignUpRequestDto
import shop.frankit.domain.member.exception.FailLoginException
import shop.frankit.domain.member.exception.FailSignUpException
import shop.frankit.domain.member.model.Member
import shop.frankit.domain.member.repository.MemberJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberJpaRepository: MemberJpaRepository,
    private val jwtComponent: JwtComponent
) {

    @Transactional
    fun signUp(requestDto: SignUpRequestDto) {
        if (memberJpaRepository.findByEmail(requestDto.email) != null) {
            throw FailSignUpException(requestDto.email)
        }

        val member = Member(email = requestDto.email, password = requestDto.password)
        memberJpaRepository.save(member)
    }

    @Transactional(readOnly = true)
    fun login(requestDto: LoginRequestDto): LoginResponseDto {
        return memberJpaRepository.findByEmailAndPassword(email = requestDto.email, password = requestDto.password)
            ?.let {
                LoginResponseDto(token = jwtComponent.createToken(email = it.email))
            }
            ?: throw FailLoginException()
    }

}
