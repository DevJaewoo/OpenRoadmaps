package com.devjaewoo.openroadmaps.domain.blog.dto;

import com.devjaewoo.openroadmaps.domain.blog.entity.Post;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import lombok.Builder;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
public record PostDto(
        Long id,
        String title,
        String content,
        String image,
        Accessibility accessibility,
        int likes,
        int views,
        Long categoryId,
        Long roadmapItemId,
        Long clientId,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate
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
                .image(post.getImage())
                .accessibility(post.getAccessibility())
                .likes(post.getLikes())
                .views(post.getViews())
                .categoryId(categoryId)
                .roadmapItemId(roadmapItemId)
                .clientId(post.getClient().getId())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .build();
    }

    @Builder
    public record Response(
            Long id,
            String title,
            String content,
            String image,
            int likes,
            int views,
            Long categoryId,
            Long roadmapItemId,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {

        public static Response from(PostDto postDto) {
            return Response.builder()
                    .id(postDto.id)
                    .title(postDto.title)
                    .content(postDto.content)
                    .image(postDto.image)
                    .likes(postDto.likes)
                    .views(postDto.views)
                    .categoryId(postDto.categoryId)
                    .roadmapItemId(postDto.roadmapItemId)
                    .createdDate(postDto.createdDate)
                    .modifiedDate(postDto.modifiedDate)
                    .build();
        }
    }

    public record CreateRequest(
            @Size(min = 1, max = 50)
            String title,
            String content,
            String image,
            Accessibility accessibility,
            Long categoryId,
            Long roadmapItemId
    ) { }

    public record CreateResponse(Long postId) { }

    @Builder
    public record ListItem(
            Long id,
            String title,
            String image,
            Accessibility accessibility,
            int likes,
            Long categoryId,
            Long roadmapItemId,
            Long clientId,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {

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
                    .image(post.getImage())
                    .accessibility(post.getAccessibility())
                    .likes(post.getLikes())
                    .categoryId(categoryId)
                    .roadmapItemId(roadmapItemId)
                    .clientId(post.getClient().getId())
                    .createdDate(post.getCreatedDate())
                    .modifiedDate(post.getModifiedDate())
                    .build();
        }
    }
}
