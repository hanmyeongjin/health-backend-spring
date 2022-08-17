package kr.co.fitpet.health.repository.item

import kr.co.fitpet.health.entity.item.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository: JpaRepository<Item, Int>