package shop.frankit.common.error.exception

import shop.frankit.common.error.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

abstract class AbstractFrankitException (
    val errorCode: ErrorCode,
    val httpStatus: HttpStatus = BAD_REQUEST,
    override val message: String? = null,
): RuntimeException()