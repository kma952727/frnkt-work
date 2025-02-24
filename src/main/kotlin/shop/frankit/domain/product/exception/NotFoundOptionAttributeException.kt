package shop.frankit.domain.product.exception

import org.springframework.http.HttpStatus.NOT_FOUND
import shop.frankit.common.error.ErrorCode.NOT_FOUND__PRODUCT_OPTION_ATTRIBUTE
import shop.frankit.common.error.exception.AbstractFrankitException

class NotFoundOptionAttributeException(attributeId: Long): AbstractFrankitException(
    errorCode = NOT_FOUND__PRODUCT_OPTION_ATTRIBUTE,
    httpStatus = NOT_FOUND,
    message = "존재하지 않는 옵션 선택지입니다. attributeId -> $attributeId"
)