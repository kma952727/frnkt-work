package shop.frankit.domain.product.exception

import org.springframework.http.HttpStatus.BAD_REQUEST
import shop.frankit.common.error.ErrorCode.OPTION_ATTRIBUTE_MISSING
import shop.frankit.common.error.exception.AbstractFrankitException

class OptionAttributeMissingException(minimumOptionSize: Int): AbstractFrankitException(
    errorCode = OPTION_ATTRIBUTE_MISSING,
    httpStatus = BAD_REQUEST,
    message = "최소 ${minimumOptionSize}개의 옵션 선택지가 있어야 합니다. "
)