package kr.co.fitpet.health

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
@EnableFeignClients
class HealthServiceApplication

fun main(args: Array<String>) {
    runApplication<HealthServiceApplication>(*args)
}
