package com.devjaewoo.openroadmaps.domain.roadmap.repository;

import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapSearch;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.devjaewoo.openroadmaps.domain.roadmap.entity.QRoadmap.roadmap;

@Repository
@RequiredArgsConstructor
public class RoadmapRepositoryImpl implements RoadmapRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Roadmap> search(RoadmapSearch roadmapSearch, Pageable pageable) {

        List<Roadmap> content = queryFactory
                .selectFrom(roadmap)
                .where(
                        clientEq(roadmapSearch.client()),
                        nameLike(roadmapSearch.name()),
                        officialEq(roadmapSearch.official()),
                        roadmap.accessibility.in(Accessibility.PUBLIC, Accessibility.PROTECTED),
                        roadmap.isDeleted.isFalse()
                )
                .orderBy(order(roadmapSearch.order()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //Count 자체에 페이징을 할 수 없음
        JPAQuery<Long> countQuery = queryFactory
                .select(roadmap.count())
                .from(roadmap)
                .where(
                        clientEq(roadmapSearch.client()),
                        nameLike(roadmapSearch.name()),
                        officialEq(roadmapSearch.official()),
                        roadmap.isDeleted.isFalse()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression clientEq(Long clientId) {
        return (clientId != null) ? roadmap.client.id.eq(clientId) : null;
    }

    private BooleanExpression nameLike(String name) {
        return (name != null) ? roadmap.title.like("%" + name + "%") : null;
    }

    private BooleanExpression officialEq(Boolean isOfficial) {
        return (isOfficial != null) ? roadmap.isOfficial.eq(isOfficial) : null;
    }

    private OrderSpecifier<?> order(RoadmapSearch.Order order) {
        if(order == null) return roadmap.createdDate.desc();
        return switch (order) {
            case LIKE -> roadmap.likes.desc();
            case LATEST -> roadmap.createdDate.desc();
        };
    }
}
