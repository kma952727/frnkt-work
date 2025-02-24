package shop.frankit.common.error.exception

import org.springframework.http.HttpStatus.UNAUTHORIZED
import shop.frankit.common.error.ErrorCode.INVALID_REQUEST__AUTH_ID

class InvalidAuthIdException(message: String? = "메시지가 없습니다."): AbstractFrankitException(
    errorCode = INVALID_REQUEST__AUTH_ID,
    httpStatus = UNAUTHORIZED,
    message = "controller에 사용자의 정보를 전달할 수 없습니다. 사유 -> $message"
)