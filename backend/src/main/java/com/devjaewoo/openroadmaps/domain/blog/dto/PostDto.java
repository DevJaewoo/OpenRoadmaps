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
        int like,
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
                .like(post.getLikes())
                .categoryId(categoryId)
                .roadmapItemId(roadmapItemId)
                .clientId(post.getClient().getId())
                .build();
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
}
