package com.devjaewoo.openroadmaps.domain.roadmap;

import com.devjaewoo.openroadmaps.global.domain.Accessibility;

import java.util.List;

public record RoadmapDto(
        Long id,
        String title,
        String image,
        Accessibility accessibility,
        boolean isOfficial,
        List<RoadmapItemDto> roadmapItemList,
        int likes,
        Long clientId
) {

    public static RoadmapDto of(Roadmap roadmap) {

        List<RoadmapItemDto> roadmapItemDtoList = roadmap.getRoadmapItemList().stream()
                .map(RoadmapItemDto::of)
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
            Accessibility accessibility,
            List<RoadmapItemDto> roadmapItemList,
            int likes) {

        public Response (RoadmapDto roadmapDto) {
            this(
                    roadmapDto.id,
                    roadmapDto.title,
                    roadmapDto.image,
                    roadmapDto.accessibility,
                    roadmapDto.roadmapItemList,
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
                Accessibility accessibility,
                boolean isOfficial,
                int likes) {

            public Response(ListItem listItem) {
                this(
                        listItem.id,
                        listItem.title,
                        listItem.image,
                        listItem.accessibility,
                        listItem.isOfficial,
                        listItem.likes);
            }
        }
    }

    public record ResponseList(List<ListItem.Response> roadmapList) { }
}
