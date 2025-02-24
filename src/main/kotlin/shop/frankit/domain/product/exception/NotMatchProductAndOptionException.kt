package shop.frankit.domain.product.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import shop.frankit.common.error.ErrorCode
import shop.frankit.common.error.ErrorCode.NOT_MATCH__PRODUCT_OPTION
import shop.frankit.common.error.exception.AbstractFrankitException

class NotMatchProductAndOptionException(optionId: Long): AbstractFrankitException(
    errorCode = NOT_MATCH__PRODUCT_OPTION,
    httpStatus = BAD_REQUEST,
    message = "다른 상품의 옵션입니다. optionId -> $optionId"
)