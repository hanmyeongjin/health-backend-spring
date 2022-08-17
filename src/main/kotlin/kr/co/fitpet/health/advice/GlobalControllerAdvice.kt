package kr.co.fitpet.health.advice

import kr.co.fitpet.health.exception.AuthorizationException
import kr.co.fitpet.health.exception.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    fun handleAuthorization(e: AuthorizationException): String {
        return when(val message = e.message) {
            null -> AuthorizationException.authorizeMessage
            else -> message!!
        }
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun handleBadRequest(e: BadRequestException): String {
        return e.errorMessage
    }
}