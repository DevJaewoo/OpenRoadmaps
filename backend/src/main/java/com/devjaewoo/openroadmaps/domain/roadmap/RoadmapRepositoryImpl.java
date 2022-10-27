package com.devjaewoo.openroadmaps.domain.roadmap;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.devjaewoo.openroadmaps.domain.roadmap.QRoadmap.*;

@Repository
@RequiredArgsConstructor
public class RoadmapRepositoryImpl implements RoadmapRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Roadmap> search(RoadmapSearch roadmapSearch, Pageable pageable) {

        return queryFactory
                .selectFrom(roadmap)
                .where(
                        clientEq(roadmapSearch.client()),
                        nameLike(roadmapSearch.name()),
                        officialEq(roadmapSearch.official()),
                        roadmap.isDeleted.isFalse()
                )
                .orderBy(order(roadmapSearch.order()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
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
