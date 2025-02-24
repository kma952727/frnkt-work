package shop.frankit.domain.member.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequestDto (
    @Schema(description = "로그인에 사용하는 이메일 (이메일 형식, 공백만 포함 불가)")
    @field:Email(message = "email은 이메일 형식이여야 합니다.")
    @field:NotBlank(message = "email은 필수 값입니다.")
    val email: String,

    @Schema(description = "로그인에 사용하는 암호 (공백만 포함 불가)")
    @field:NotBlank(message = "password는 필수 값입니다.")
    val password: String
)