package shop.frankit.domain.member.exception

import shop.frankit.common.error.ErrorCode.NOT_FOUND__USER
import shop.frankit.common.error.exception.AbstractFrankitException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class NotFoundMemberException(condition: String): AbstractFrankitException(
    message = "존재하지 않는 사용자입니다. condition -> $condition",
    errorCode = NOT_FOUND__USER
)