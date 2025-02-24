package shop.frankit.domain.product.exception

import org.springframework.http.HttpStatus.NOT_FOUND
import shop.frankit.common.error.ErrorCode.NOT_FOUND__PRODUCT_OPTION
import shop.frankit.common.error.exception.AbstractFrankitException

class NotFoundProductOptionException(optionId: Long): AbstractFrankitException(
    errorCode = NOT_FOUND__PRODUCT_OPTION,
    httpStatus = NOT_FOUND,
    message = "존재하지 않는 상품 옵션입니다. optionId -> $optionId"
)