package shop.frankit.domain.member.exception

import shop.frankit.common.error.ErrorCode.FAILED_LOGIN
import shop.frankit.common.error.exception.AbstractFrankitException
import org.springframework.http.HttpStatus.BAD_REQUEST

class FailLoginException: AbstractFrankitException(
    errorCode = FAILED_LOGIN,
    httpStatus = BAD_REQUEST,
    message = "로그인에 실패하였습니다."
)