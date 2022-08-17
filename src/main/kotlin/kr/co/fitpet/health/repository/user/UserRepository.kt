package kr.co.fitpet.health.repository.user

import kr.co.fitpet.health.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int>