package shop.frankit.common.error.exception

import org.springframework.http.HttpStatus.UNAUTHORIZED
import shop.frankit.common.error.ErrorCode.INVALID_REQUEST__AUTH_TOKEN

class InvalidAuthTokenException(token: String? = "토큰 없음"): AbstractFrankitException(
    errorCode = INVALID_REQUEST__AUTH_TOKEN,
    httpStatus = UNAUTHORIZED,
    message = "토큰 데이터를 확인해주세요. token -> $token"
)