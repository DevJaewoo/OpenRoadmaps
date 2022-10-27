package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;

import java.util.List;

public record RoadmapDto(
        Long id,
        String title,
        String image,
        Accessibility accessibility,
        boolean isOfficial,
        List<RoadmapItemDto.ListItem> roadmapItemList,
        int likes,
        Long clientId
) {

    public static RoadmapDto of(Roadmap roadmap) {

        List<RoadmapItemDto.ListItem> roadmapItemDtoList = roadmap.getRoadmapItemList().stream()
                .map(RoadmapItemDto.ListItem::of)
                .toList();

        Long clientId = null;
        if(roadmap.getClient() != null) {
            clientId = roadmap.getClient().getId();
        }

        return new RoadmapDto(
                roadmap.getId(),
                roadmap.getTitle(),
                roadmap.getImage(),
                roadmap.getAccessibility(),
                roadmap.isOfficial(),
                roadmapItemDtoList,
                roadmap.getLikes(),
                clientId);
    }

    public record Response(
            Long id,
            String title,
            String image,
            String accessibility,
            List<RoadmapItemDto.ListItem.Response> roadmapItemList,
            int likes) {

        public static Response of(RoadmapDto roadmapDto) {

            // RoadmapItem ListItem을 ListItem.Response로 변환
            List<RoadmapItemDto.ListItem.Response> roadmapItemList = roadmapDto.roadmapItemList.stream()
                    .map(RoadmapItemDto.ListItem.Response::of)
                    .toList();

            return new Response(
                    roadmapDto.id,
                    roadmapDto.title,
                    roadmapDto.image,
                    roadmapDto.accessibility.name(),
                    roadmapItemList,
                    roadmapDto.likes);
        }
    }

    public record ListItem(
            Long id,
            String title,
            String image,
            Accessibility accessibility,
            boolean isOfficial,
            int likes,
            Long clientId) {

        public static ListItem of(Roadmap roadmap) {

            Long clientId = null;
            if(roadmap.getClient() != null) {
                clientId = roadmap.getClient().getId();
            }

            return new ListItem(
                    roadmap.getId(),
                    roadmap.getTitle(),
                    roadmap.getImage(),
                    roadmap.getAccessibility(),
                    roadmap.isOfficial(),
                    roadmap.getLikes(),
                    clientId);
        }

        public record Response(
                Long id,
                String title,
                String image,
                String accessibility,
                boolean isOfficial,
                int likes,
                Long clientId) {

            public static Response of(ListItem listItem) {

                String accessibility = null;
                if(listItem.accessibility != null) {
                    accessibility = listItem.accessibility.name();
                }

                return new Response(
                        listItem.id,
                        listItem.title,
                        listItem.image,
                        accessibility,
                        listItem.isOfficial,
                        listItem.likes,
                        listItem.clientId);
            }
        }
    }

    public record ResponseList(List<ListItem.Response> roadmapList) { }
}
