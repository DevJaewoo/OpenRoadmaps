package com.devjaewoo.openroadmaps.domain.blog.dto;


import com.devjaewoo.openroadmaps.domain.blog.entity.PostLike;
import lombok.Builder;

@Builder
public record PostLikeDto(
        Long id,
        int likes,
        boolean liked,
        Long postId,
        Long clientId
) {

    public static PostLikeDto from(PostLike postLike, int likes) {
        return PostLikeDto.builder()
                .id(postLike.getId())
                .likes(likes)
                .liked(postLike.isLike())
                .postId(postLike.getPost().getId())
                .clientId(postLike.getClient().getId())
                .build();
    }

    public record LikeRequest(boolean liked) { }

    @Builder
    public record LikeResponse(Long postId, boolean liked, int likes) {

        public static PostLikeDto.LikeResponse from(PostLikeDto postLikeDto) {
            return PostLikeDto.LikeResponse.builder()
                    .postId(postLikeDto.postId)
                    .liked(postLikeDto.liked)
                    .likes(postLikeDto.likes)
                    .build();
        }
    }
}
