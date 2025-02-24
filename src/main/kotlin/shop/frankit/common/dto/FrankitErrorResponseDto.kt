package shop.frankit.common.dto

import shop.frankit.common.error.ErrorCode

data class FrankitErrorResponseDto (
    val errorCode: ErrorCode,
    val resultMsg: String? = null,
)