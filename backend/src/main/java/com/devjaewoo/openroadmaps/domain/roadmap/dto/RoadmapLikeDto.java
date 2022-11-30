package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapLike;

public record RoadmapLikeDto(Long id, Long roadmapId, Long clientId, boolean liked, int likes) {

    public static RoadmapLikeDto from(RoadmapLike roadmapLike, int likes) {
        return new RoadmapLikeDto(
                roadmapLike.getId(),
                roadmapLike.getRoadmap().getId(),
                roadmapLike.getClient().getId(),
                roadmapLike.isLike(),
                likes
        );
    }

    public record LikeRequest(boolean liked) { }

    public record LikeResponse(Long roadmapId, boolean liked, int likes) {

        public static LikeResponse from(RoadmapLikeDto roadmapLikeDto) {
            return new LikeResponse(roadmapLikeDto.roadmapId, roadmapLikeDto.liked, roadmapLikeDto.likes);
        }
    }
}
