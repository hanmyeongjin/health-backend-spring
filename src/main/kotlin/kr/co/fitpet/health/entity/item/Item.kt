package kr.co.fitpet.health.entity.item

import kr.co.fitpet.health.entity.base.BaseDateTime
import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "item")
@DynamicUpdate
data class Item(

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "name")
    val name: String,

    @Column(name = "`key`")
    val key: String,

    @Column(name = "cycle_type")
    val cycleType: String,

    @Column(name = "icon_url")
    val iconUrl: String,

    @Column(name = "title")
    val title: String,

    @Column(name = "title2")
    val title2: String,

    @Column(name = "point")
    val point: Int,

    @Column(name = "max_point")
    val maxPoint: Int,

    @Column(name = "is_opened")
    val isOpened: Int,

    @Column(name = "position")
    val position: Int,

    @Column(name = "memo")
    val memo: String? = null,

) : BaseDateTime(), Serializable