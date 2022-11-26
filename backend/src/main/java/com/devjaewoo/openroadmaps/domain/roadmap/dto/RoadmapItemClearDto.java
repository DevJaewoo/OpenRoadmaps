package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItemClear;

public record RoadmapItemClearDto(Long id, Long roadmapItemId, Long clientId, boolean isCleared) {

    public static RoadmapItemClearDto from(RoadmapItemClear roadmapItemClear) {
        return new RoadmapItemClearDto(
                roadmapItemClear.getId(),
                roadmapItemClear.getRoadmapItem().getId(),
                roadmapItemClear.getClient().getId(),
                roadmapItemClear.isCleared()
        );
    }

    public record ClearRequest(boolean isCleared) { }

    public record ClearResponse(Long roadmapItemId, boolean isCleared) {
        public static ClearResponse from(RoadmapItemClearDto roadmapItemClearDto) {
            return new ClearResponse(roadmapItemClearDto.roadmapItemId, roadmapItemClearDto.isCleared);
        }
    }
}
