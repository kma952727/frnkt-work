package shop.frankit.domain.member.exception

import shop.frankit.common.error.ErrorCode.FAILED_SIGN_UP__DUPLICATED_EMAIL
import shop.frankit.common.error.exception.AbstractFrankitException
import org.springframework.http.HttpStatus.BAD_REQUEST

class FailSignUpException(email: String): AbstractFrankitException(
    errorCode = FAILED_SIGN_UP__DUPLICATED_EMAIL,
    httpStatus = BAD_REQUEST,
    message = "회원 가입에 실패하였습니다. 중복된 이메일 : $email"
)