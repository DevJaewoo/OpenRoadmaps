package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItemClear;

public record RoadmapItemClearDto(Long id, Long roadmapItemId, Long clientId, boolean isCleared) {

    public static RoadmapItemClearDto of(RoadmapItemClear roadmapItemClear) {
        return new RoadmapItemClearDto(
                roadmapItemClear.getId(),
                roadmapItemClear.getRoadmapItem().getId(),
                roadmapItemClear.getClient().getId(),
                roadmapItemClear.isCleared()
        );
    }

    public record ClearRequest(boolean isCleared) { }

    public record ClearResponse(Long id, boolean isCleared) {
        public static ClearResponse of(RoadmapItemClearDto roadmapItemClearDto) {
            return new ClearResponse(roadmapItemClearDto.id, roadmapItemClearDto.isCleared);
        }
    }
}
