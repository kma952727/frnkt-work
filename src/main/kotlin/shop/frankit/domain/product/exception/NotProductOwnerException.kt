package shop.frankit.domain.product.exception

import shop.frankit.common.error.ErrorCode.FAILED_UPDATE_PRODUCT__NOT_OWNER
import shop.frankit.common.error.exception.AbstractFrankitException

class NotProductOwnerException(productId: Long, requestMemberId: Long): AbstractFrankitException(
    message = "상품의 주인이 아닙니다. productId -> $productId, requestMemberId -> $requestMemberId",
    errorCode = FAILED_UPDATE_PRODUCT__NOT_OWNER
)