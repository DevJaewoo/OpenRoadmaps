package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public record RoadmapDto(
        Long id,
        String title,
        String image,
        Accessibility accessibility,
        boolean isOfficial,
        List<RoadmapItemDto.ListItem> roadmapItemList,
        int likes,
        LocalDateTime createdDate,
        Long clientId
) {

    public static RoadmapDto from(Roadmap roadmap) {
        List<Long> empty = List.of();
        return from(roadmap, empty);
    }

    public static RoadmapDto from(Roadmap roadmap, List<Long> roadmapItemClearList) {

        List<RoadmapItemDto.ListItem> roadmapItemDtoList = roadmap.getRoadmapItemList().stream()
                .map(r -> {
                    boolean isCleared = r.getId() != null && roadmapItemClearList.contains(r.getId());
                    return RoadmapItemDto.ListItem.from(r, isCleared);
                })
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
                roadmap.getCreatedDate(),
                clientId);
    }

    public record Response(
            Long id,
            String title,
            String image,
            String accessibility,
            int likes,
            String createdDate,
            List<RoadmapItemDto.ListItem.Response> roadmapItemList) {

        public static Response from(RoadmapDto roadmapDto) {

            // RoadmapItem ListItem을 ListItem.Response로 변환
            List<RoadmapItemDto.ListItem.Response> roadmapItemList = roadmapDto.roadmapItemList.stream()
                    .map(RoadmapItemDto.ListItem.Response::from)
                    .toList();

            String createdDate = LocalDateTime.MIN.toString();
            if(roadmapDto.createdDate != null) {
                createdDate = roadmapDto.createdDate.toString();
            }

            return new Response(
                    roadmapDto.id,
                    roadmapDto.title,
                    roadmapDto.image,
                    roadmapDto.accessibility.name(),
                    roadmapDto.likes,
                    createdDate,
                    roadmapItemList);
        }
    }

    public record ListItem(
            Long id,
            String title,
            String image,
            Accessibility accessibility,
            boolean isOfficial,
            int likes,
            LocalDateTime createdDate,
            Long clientId) {

        public static ListItem from(Roadmap roadmap) {

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
                    roadmap.getCreatedDate(),
                    clientId);
        }

        public record Response(
                Long id,
                String title,
                String image,
                String accessibility,
                boolean isOfficial,
                int likes,
                String createdDate,
                Long clientId) {

            public static Response from(ListItem listItem) {

                String accessibility = null;
                if(listItem.accessibility != null) {
                    accessibility = listItem.accessibility.name();
                }

                String createdDate = LocalDateTime.MIN.toString();
                if(listItem.createdDate != null) {
                    createdDate = listItem.createdDate.toString();
                }

                return new Response(
                        listItem.id,
                        listItem.title,
                        listItem.image,
                        accessibility,
                        listItem.isOfficial,
                        listItem.likes,
                        createdDate,
                        listItem.clientId);
            }
        }
    }

    public record CreateRequest(
            @NotNull
            @Size(min = 1, max = 50)
            String title,
            String image,
            Accessibility accessibility,
            @NotNull List<RoadmapItemDto.CreateRequest> roadmapItemList) { }

    public record CreateResponse (
            Long roadmapId
    ) { }
}
