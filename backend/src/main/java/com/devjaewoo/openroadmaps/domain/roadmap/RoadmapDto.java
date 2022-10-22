package com.devjaewoo.openroadmaps.domain.roadmap;

import com.devjaewoo.openroadmaps.global.domain.Accessibility;

import java.util.List;

public record RoadmapDto(
        Long id,
        String title,
        Accessibility accessibility,
        boolean isOfficial,
        List<RoadmapItem> roadmapItemList,
        Long clientId
) {

    public RoadmapDto(Roadmap roadmap) {
        this(
                roadmap.getId(),
                roadmap.getTitle(),
                roadmap.getAccessibility(),
                roadmap.isOfficial(),
                roadmap.getRoadmapItemList(),
                roadmap.getId());
    }

    public record Response(
            Long id,
            String title,
            Accessibility accessibility,
            boolean isOfficial,
            List<RoadmapItem> roadmapItemList,
            int like) {

    }

    public record ResponseList(List<Response> roadmapList) { }
}
