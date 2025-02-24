package shop.frankit.domain.product.exception

import shop.frankit.common.error.ErrorCode.NOT_FOUND__PRODUCT
import shop.frankit.common.error.exception.AbstractFrankitException

class NotFoundProductException(productId: Long): AbstractFrankitException(
    message = "존재하지 않는 상품입니다. productId -> $productId",
    errorCode = NOT_FOUND__PRODUCT
)