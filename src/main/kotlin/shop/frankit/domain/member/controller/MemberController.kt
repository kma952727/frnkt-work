package shop.frankit.domain.member.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import shop.frankit.common.dto.FrankitResponseDto
import shop.frankit.domain.member.dto.LoginRequestDto
import shop.frankit.domain.member.dto.LoginResponseDto
import shop.frankit.domain.member.dto.SignUpRequestDto
import shop.frankit.domain.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "사용자 정보 관리")
@RestController
@RequestMapping("/api")
class MemberController(
    private val memberService: MemberService
) {

    @Operation(summary = "회원 가입")
    @PostMapping("/members/sign-up")
    fun signUp(@Valid @RequestBody requestDto: SignUpRequestDto): ResponseEntity<FrankitResponseDto<Nothing>> {
        memberService.signUp(requestDto = requestDto)

        return ResponseEntity.ok(FrankitResponseDto())
    }

    @Operation(summary = "로그인")
    @PostMapping("/members/login")
    fun login(@Valid @RequestBody requestDto: LoginRequestDto): ResponseEntity<FrankitResponseDto<LoginResponseDto>> {
        val result = memberService.login(requestDto = requestDto)

        return ResponseEntity.ok(FrankitResponseDto(data = result))
    }
}