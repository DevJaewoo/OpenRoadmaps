package com.devjaewoo.openroadmaps.domain.blog.repository;

import com.devjaewoo.openroadmaps.domain.blog.dto.PostSearch;
import com.devjaewoo.openroadmaps.domain.blog.entity.Post;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.devjaewoo.openroadmaps.domain.blog.entity.QPost.post;


@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> search(PostSearch postSearch, Pageable pageable, Long currentClientId) {

        List<Post> content = queryFactory
                .selectFrom(post)
                .where(
                        titleLike(postSearch.title()),
                        clientNameEq(postSearch.clientName()),
                        categoryEq(postSearch.categoryId()),
                        roadmapItemEq(postSearch.roadmapItemId()),
                        isAccessible(currentClientId),
                        post.isDeleted.isFalse()
                )
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //Count 자체에 페이징을 할 수 없음
        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        titleLike(postSearch.title()),
                        clientNameEq(postSearch.clientName()),
                        categoryEq(postSearch.categoryId()),
                        roadmapItemEq(postSearch.roadmapItemId()),
                        isAccessible(currentClientId),
                        post.isDeleted.isFalse()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression titleLike(String title) {
        return (title != null) ? post.title.like("%" + title + "%") : null;
    }

    private BooleanExpression clientNameEq(String clientName) {
        return (clientName != null) ? post.client.name.eq(clientName) : null;
    }

    private BooleanExpression categoryEq(Long categoryId) {
        return (categoryId != null) ? post.category.id.eq(categoryId) : null;
    }

    private BooleanExpression roadmapItemEq(Long roadmapItemId) {
        return (roadmapItemId != null) ? post.roadmapItem.id.eq(roadmapItemId) : null;
    }

    private BooleanExpression isAccessible(Long clientId) {
        BooleanExpression defaultAccessibility = post.accessibility.in(Accessibility.PUBLIC, Accessibility.PROTECTED);
        return (clientId != null) ? defaultAccessibility.or(post.client.id.eq(clientId)) : defaultAccessibility;
    }
}
