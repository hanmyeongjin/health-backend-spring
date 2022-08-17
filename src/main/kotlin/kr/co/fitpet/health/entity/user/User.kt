package kr.co.fitpet.health.entity.user

import kr.co.fitpet.health.entity.base.BaseDateTime
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "`user`")
@DynamicUpdate
data class User(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @Column(name = "fitpetmall_user_id")
    var fitpetmallUserId: Int,

    @Column(name = "username")
    var username: String,

    @Column(name = "email")
    var email: String,

    @Column(name = "phone")
    var phone: String,

    @Column(name = "applied_item_count")
    var appliedItemCount: Int,

    @Column(name = "applied_mission_count")
    var appliedMissionCount: Int,

    @Column(name = "`name`")
    var memo: String,

    @Column(name = "latest_synced_at")
    var latestSyncedAt: LocalDateTime? = null,

    @Column(name = "latest_visited_at")
    var latestVisitedAt: LocalDateTime? = null

): BaseDateTime(), Serializable