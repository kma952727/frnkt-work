package shop.frankit.common.error.exception

import shop.frankit.common.error.ErrorCode.UNKNOWN_ERROR
import org.springframework.http.HttpStatus.BAD_REQUEST

class FailAuthenticationException(cause: String): AbstractFrankitException(
    errorCode = UNKNOWN_ERROR,
    httpStatus = BAD_REQUEST,
    message = "인증에 실패하였습니다. 사유 : $cause"
)