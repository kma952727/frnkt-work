package shop.frankit.domain.product.exception

import org.springframework.http.HttpStatus.BAD_REQUEST
import shop.frankit.common.error.ErrorCode.TOO_MANY__PRODUCT_OPTION_ATTRIBUTE
import shop.frankit.common.error.exception.AbstractFrankitException

class TooManyOptionAttributeException(maximumSize: Int): AbstractFrankitException(
    errorCode = TOO_MANY__PRODUCT_OPTION_ATTRIBUTE,
    httpStatus = BAD_REQUEST,
    message = "옵션 선택자가 너무 많습니다. 옵션 최대 수 : $maximumSize"
)