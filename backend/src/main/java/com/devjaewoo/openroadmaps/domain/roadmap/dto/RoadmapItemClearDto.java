package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItemClear;
import lombok.Builder;

@Builder
public record RoadmapItemClearDto(Long id, Long roadmapItemId, Long clientId, boolean isCleared) {

    public static RoadmapItemClearDto from(RoadmapItemClear roadmapItemClear) {
        return RoadmapItemClearDto.builder()
                .id(roadmapItemClear.getId())
                .roadmapItemId(roadmapItemClear.getRoadmapItem().getId())
                .clientId(roadmapItemClear.getClient().getId())
                .isCleared(roadmapItemClear.isCleared())
                .build();
    }

    public record ClearRequest(boolean isCleared) { }

    @Builder
    public record ClearResponse(Long roadmapItemId, boolean isCleared) {
        public static ClearResponse from(RoadmapItemClearDto roadmapItemClearDto) {
            return ClearResponse.builder()
                    .roadmapItemId(roadmapItemClearDto.roadmapItemId)
                    .isCleared(roadmapItemClearDto.isCleared)
                    .build();
        }
    }
}
