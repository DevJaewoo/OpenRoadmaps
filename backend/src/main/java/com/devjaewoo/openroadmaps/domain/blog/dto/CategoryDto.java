package com.devjaewoo.openroadmaps.domain.blog.dto;

import com.devjaewoo.openroadmaps.domain.blog.entity.Category;
import lombok.Builder;

import java.util.List;

@Builder
public record CategoryDto(
        Long id,
        String name,
        List<PostDto.ListItem> postList,
        Long clientId) {

    public static CategoryDto from(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .postList(
                        category.getPostList().stream()
                                .map(PostDto.ListItem::from)
                                .toList()
                )
                .clientId(category.getClient().getId())
                .build();
    }

    public record CreateRequest(String name) { }
    public record CreateResponse(Long categoryId) { }
    public record DeleteResponse(Long categoryId) { }

    @Builder
    public record ListItem(
            Long id,
            String name,
            Long clientId) {

        public static ListItem from(Category category) {
            return ListItem.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .clientId(category.getClient().getId())
                    .build();
        }

        @Builder
        public record Response(Long id, String name) {

            public static Response from(ListItem listItem) {
                return Response.builder()
                        .id(listItem.id)
                        .name(listItem.name)
                        .build();
            }
        }
    }
}
