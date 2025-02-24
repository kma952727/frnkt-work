package shop.frankit.domain.product.exception

import org.springframework.http.HttpStatus.CONFLICT
import shop.frankit.common.error.ErrorCode.CONFLICT__PRODUCT_OPTION_NAME
import shop.frankit.common.error.exception.AbstractFrankitException

class ConflictProductOptionNameException(optionName: String): AbstractFrankitException(
    errorCode = CONFLICT__PRODUCT_OPTION_NAME,
    httpStatus = CONFLICT,
    message = "중복된 옵션 이름입니다. optionName -> $optionName"
)