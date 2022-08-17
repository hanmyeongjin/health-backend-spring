package kr.co.fitpet.health.dto.item

import kr.co.fitpet.health.entity.item.Item
import java.io.Serializable
import java.time.LocalDateTime

data class ItemResponse(

    val id: Int? = null,

    val name: String? = "",

    val key: String = "",

    val cycleType: String? = "",

    val iconUrl: String? = "",

    val title: String? = "",

    val title2: String? = "",

    val point: Int? = null,

    val maxPoint: Int? = null,

    val isOpened: Int? = null,

    val position: Int? = null,

    val memo: String? = "",

    val createdAt: LocalDateTime? = null,

    val updatedAt: LocalDateTime? = null,
): Serializable {

    companion object {

        fun getItemResponse(item: Item): ItemResponse {
            return ItemResponse(
                id = item.id,
                name = item.name,
                key = item.key,
                cycleType =  item.cycleType,
                iconUrl = item.iconUrl,
                title = item.title,
                title2 = item.title2,
                point = item.point,
                maxPoint = item.maxPoint,
                isOpened = item.isOpened,
                position = item.position,
                memo = item.memo,
                createdAt = item.createdAt,
                updatedAt = item.updatedAt
            )
        }
    }
}