package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapLike;
import lombok.Builder;

@Builder
public record RoadmapLikeDto(Long id, Long roadmapId, Long clientId, boolean liked, int likes) {

    public static RoadmapLikeDto from(RoadmapLike roadmapLike, int likes) {
        return RoadmapLikeDto.builder()
                .id(roadmapLike.getId())
                .roadmapId(roadmapLike.getRoadmap().getId())
                .clientId(roadmapLike.getClient().getId())
                .liked(roadmapLike.isLike())
                .likes(likes)
                .build();
    }

    public record LikeRequest(boolean liked) { }

    @Builder
    public record LikeResponse(Long roadmapId, boolean liked, int likes) {

        public static LikeResponse from(RoadmapLikeDto roadmapLikeDto) {
            return LikeResponse.builder()
                    .roadmapId(roadmapLikeDto.roadmapId)
                    .liked(roadmapLikeDto.liked)
                    .likes(roadmapLikeDto.likes)
                    .build();
        }
    }
}
