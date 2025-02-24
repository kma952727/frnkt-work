package shop.frankit.common.dto

import io.swagger.v3.oas.annotations.media.Schema

data class FrankitResponseDto<T> (

    @Schema(description = "응답 메시지")
    val resultMsg: String? = "OK",

    @Schema(description = "응답 컨텐츠")
    val data: T? = null
)