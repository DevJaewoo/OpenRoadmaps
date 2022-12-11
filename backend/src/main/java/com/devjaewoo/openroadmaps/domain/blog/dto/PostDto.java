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
        boolean liked,
        int views,
        Long categoryId,
        Long roadmapItemId,
        Long clientId,
        String clientName,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate
) {

    public static PostDto from(Post post) {
        return from(post, false);
    }

    public static PostDto from(Post post, boolean liked) {

        Long categoryId = null;
        if(post.getCategory() != null) {
            categoryId = post.getCategory().getId();
        }

        Long roadmapItemId = null;
        if(post.getRoadmapItem() != null) {
            roadmapItemId = post.getRoadmapItem().getId();
        }

        Long clientId = null;
        String clientName = "Anonymous";
        if(post.getClient() != null) {
            clientId = post.getClient().getId();
            clientName = post.getClient().getName();
        }

        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .image(post.getImage())
                .accessibility(post.getAccessibility())
                .likes(post.getLikes())
                .liked(liked)
                .views(post.getViews())
                .categoryId(categoryId)
                .roadmapItemId(roadmapItemId)
                .clientId(clientId)
                .clientName(clientName)
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
            boolean liked,
            int views,
            Long categoryId,
            Long roadmapItemId,
            Long clientId,
            String clientName,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {

        public static Response from(PostDto postDto) {
            return Response.builder()
                    .id(postDto.id)
                    .title(postDto.title)
                    .content(postDto.content)
                    .image(postDto.image)
                    .likes(postDto.likes)
                    .liked(postDto.liked)
                    .views(postDto.views)
                    .categoryId(postDto.categoryId)
                    .roadmapItemId(postDto.roadmapItemId)
                    .clientId(postDto.clientId)
                    .clientName(postDto.clientName)
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
            String clientName,
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

            Long clientId = null;
            String clientName = "Anonymous";
            if(post.getClient() != null) {
                clientId = post.getClient().getId();
                clientName = post.getClient().getName();
            }

            return ListItem.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .image(post.getImage())
                    .accessibility(post.getAccessibility())
                    .likes(post.getLikes())
                    .categoryId(categoryId)
                    .roadmapItemId(roadmapItemId)
                    .clientId(clientId)
                    .clientName(clientName)
                    .createdDate(post.getCreatedDate())
                    .modifiedDate(post.getModifiedDate())
                    .build();
        }
    }
}
