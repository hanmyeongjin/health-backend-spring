package kr.co.fitpet.health.repository.user

import kr.co.fitpet.health.dto.user.UserCash
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRedisRepository: CrudRepository<UserCash, String>