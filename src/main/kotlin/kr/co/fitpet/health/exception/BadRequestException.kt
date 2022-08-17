package kr.co.fitpet.health.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
class BadRequestException(val errorMessage: String) : Throwable()