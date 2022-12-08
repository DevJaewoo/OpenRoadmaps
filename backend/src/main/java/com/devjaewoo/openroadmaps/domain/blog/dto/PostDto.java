package com.devjaewoo.openroadmaps.domain.blog.dto;

import com.devjaewoo.openroadmaps.domain.blog.entity.Post;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import lombok.Builder;

import javax.validation.constraints.Size;

@Builder
public record PostDto(
        Long id,
        String title,
        String content,
        Accessibility accessibility,
        int likes,
        int views,
        Long categoryId,
        Long roadmapItemId,
        Long clientId
) {

    public static PostDto from(Post post) {

        Long categoryId = null;
        if(post.getCategory() != null) {
            categoryId = post.getCategory().getId();
        }

        Long roadmapItemId = null;
        if(post.getRoadmapItem() != null) {
            roadmapItemId = post.getRoadmapItem().getId();
        }

        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .accessibility(post.getAccessibility())
                .likes(post.getLikes())
                .views(post.getViews())
                .categoryId(categoryId)
                .roadmapItemId(roadmapItemId)
                .clientId(post.getClient().getId())
                .build();
    }

    @Builder
    public record Response(
            Long id,
            String title,
            String content,
            int likes,
            int views,
            Long categoryId,
            Long roadmapItemId) {

        public static Response from(PostDto postDto) {
            return Response.builder()
                    .id(postDto.id)
                    .title(postDto.title)
                    .content(postDto.content)
                    .likes(postDto.likes)
                    .views(postDto.views)
                    .categoryId(postDto.categoryId)
                    .roadmapItemId(postDto.roadmapItemId)
                    .build();
        }
    }

    public record CreateRequest(
            @Size(min = 1, max = 50)
            String title,
            String content,
            Accessibility accessibility,
            Long categoryId,
            Long roadmapItemId
    ) { }

    public record CreateResponse(Long postId) { }

    @Builder
    public record ListItem(
            Long id,
            String title,
            Accessibility accessibility,
            int likes,
            Long categoryId,
            Long roadmapItemId,
            Long clientId) {

        public static ListItem from(Post post) {
            Long categoryId = null;
            if(post.getCategory() != null) {
                categoryId = post.getCategory().getId();
            }

            Long roadmapItemId = null;
            if(post.getRoadmapItem() != null) {
                roadmapItemId = post.getRoadmapItem().getId();
            }

            return ListItem.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .accessibility(post.getAccessibility())
                    .likes(post.getLikes())
                    .categoryId(categoryId)
                    .roadmapItemId(roadmapItemId)
                    .clientId(post.getClient().getId())
                    .build();
        }
    }
}
