package shop.frankit.domain.member.dto

import io.swagger.v3.oas.annotations.media.Schema

data class LoginResponseDto (
    @Schema(description = "인증에 사용할 수 있는 토큰, 헤더에 포함해주세요.")
    val token: String
)