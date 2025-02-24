package shop.frankit.common.component

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.slf4j.LoggerFactory
import shop.frankit.common.error.exception.AbstractFrankitException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import shop.frankit.common.dto.FrankitErrorResponseDto
import shop.frankit.common.error.ErrorCode.*

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleIllegalArgumentException(e: MethodArgumentNotValidException): ResponseEntity<FrankitErrorResponseDto> {
        val messages = e.bindingResult.allErrors.map { it.defaultMessage }
        log.warn("handleIllegalArgumentException { message : \"${e.message}\", message2: \"$messages\" }")

        return ResponseEntity
            .status(BAD_REQUEST)
            .body(FrankitErrorResponseDto(errorCode = REQUEST_DATA_ERROR, resultMsg = messages.toString()))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<FrankitErrorResponseDto> {
        val resultMessage = when (e.cause) {
            is MismatchedInputException -> {
                "${(e.cause as MismatchedInputException).path[0].fieldName} 데이터를 확인해주세요."
            }
            else -> e.cause?.toString() ?: "알 수 없는 에러"
        }

        log.warn("handleHttpMessageNotReadableException { message : \"${e.message}\", message2: \"$resultMessage\" }")

        return ResponseEntity
            .status(BAD_REQUEST)
            .body(FrankitErrorResponseDto(errorCode = REQUEST_DATA_ERROR, resultMsg = resultMessage))
    }

    @ExceptionHandler(AbstractFrankitException::class)
    fun handleFrankitException(e: AbstractFrankitException): ResponseEntity<FrankitErrorResponseDto> {
        log.warn("handleFrankitException { message: \"${e.message}\" }")

        return ResponseEntity
            .status(e.httpStatus)
            .body(FrankitErrorResponseDto(errorCode = e.errorCode, resultMsg = e.message))
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleServerException(e: RuntimeException): ResponseEntity<FrankitErrorResponseDto> {
        log.error("handleServerException", e)

        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(FrankitErrorResponseDto(errorCode = SERVER_ERROR, resultMsg = "서버 에러"))
    }
}