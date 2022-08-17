package kr.co.fitpet.health.exception

class RestApiServerException : RuntimeException {
    constructor(message: String?) : super(message)
    constructor() : super("API 서버 접속 실패")
}