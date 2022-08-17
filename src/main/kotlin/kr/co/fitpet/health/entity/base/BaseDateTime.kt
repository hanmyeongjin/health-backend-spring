package kr.co.fitpet.health.entity.base

import com.sun.istack.NotNull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseDateTime {

    @CreatedDate
    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null

    @NotNull
    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
}