package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapLike;

public record RoadmapLikeDto(Long id, Long roadmapId, Long clientId, boolean like) {

    public static RoadmapLikeDto of(RoadmapLike roadmapLike) {
        return new RoadmapLikeDto(
                roadmapLike.getId(),
                roadmapLike.getRoadmap().getId(),
                roadmapLike.getClient().getId(),
                roadmapLike.isLike()
        );
    }

    public record LikeRequest(boolean like) { }

    public record LikeResponse(Long roadmapId, boolean like) {

        public static LikeResponse of(RoadmapLikeDto roadmapLikeDto) {
            return new LikeResponse(roadmapLikeDto.roadmapId, roadmapLikeDto.like);
        }
    }
}
