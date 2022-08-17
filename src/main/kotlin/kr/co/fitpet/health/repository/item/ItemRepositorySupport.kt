package kr.co.fitpet.health.repository.item

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.co.fitpet.health.entity.item.Item
import kr.co.fitpet.health.entity.item.QItem.item
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository


@Repository
class ItemRepositorySupport(
    val queryFactory: JPAQueryFactory
): QuerydslRepositorySupport(Item::class.java)  {

//    val log = KotlinLogging.logger {}

    fun getOpenItemList(page: Pageable): PageImpl<Item> {
        val query = querydsl!!.applyPagination(
            page,
            queryFactory
                .selectFrom(item)
                .where(item.isOpened.eq(1))
                .orderBy(item.position.desc())
        )

        val itemList = query.fetch()
        val totalCount = query.fetchCount() // 추후 데이터가 많다면 카운트 쿼리 최적화 필요

        return PageImpl(itemList, page, totalCount)
    }
}