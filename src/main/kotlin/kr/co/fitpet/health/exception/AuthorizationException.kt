package kr.co.fitpet.health.exception

class AuthorizationException : RuntimeException {

    companion object {
        const val authorizeMessage = "[토큰 오류] 사용자 정보를 찾을 수 없습니다."

    }

    constructor(message: String?) : super(message)
    constructor() : super(authorizeMessage)
}