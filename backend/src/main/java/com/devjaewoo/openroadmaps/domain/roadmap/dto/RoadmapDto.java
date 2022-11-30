package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RoadmapDto(
        Long id,
        String title,
        String image,
        Accessibility accessibility,
        boolean isOfficial,
        List<RoadmapItemDto.ListItem> roadmapItemList,
        int likes,
        boolean liked,
        LocalDateTime createdDate,
        Long clientId
) {

    public static RoadmapDto from(Roadmap roadmap) {
        List<Long> empty = List.of();
        return from(roadmap, false, empty);
    }

    public static RoadmapDto from(Roadmap roadmap, boolean liked,  List<Long> roadmapItemClearList) {

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

        return RoadmapDto.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .image(roadmap.getImage())
                .accessibility(roadmap.getAccessibility())
                .isOfficial(roadmap.isOfficial())
                .roadmapItemList(roadmapItemDtoList)
                .likes(roadmap.getLikes())
                .liked(liked)
                .createdDate(roadmap.getCreatedDate())
                .clientId(clientId)
                .build();
    }

    @Builder
    public record Response(
            Long id,
            String title,
            String image,
            String accessibility,
            int likes,
            boolean liked,
            String createdDate,
            List<RoadmapItemDto.ListItem.Response> roadmapItemList,
            Long clientId) {

        public static Response from(RoadmapDto roadmapDto) {

            // RoadmapItem ListItem을 ListItem.Response로 변환
            List<RoadmapItemDto.ListItem.Response> roadmapItemList = roadmapDto.roadmapItemList.stream()
                    .map(RoadmapItemDto.ListItem.Response::from)
                    .toList();

            String createdDate = LocalDateTime.MIN.toString();
            if(roadmapDto.createdDate != null) {
                createdDate = roadmapDto.createdDate.toString();
            }

            return Response.builder()
                    .id(roadmapDto.id)
                    .title(roadmapDto.title)
                    .image(roadmapDto.image)
                    .accessibility(roadmapDto.accessibility.name())
                    .likes(roadmapDto.likes)
                    .liked(roadmapDto.liked)
                    .createdDate(createdDate)
                    .roadmapItemList(roadmapItemList)
                    .clientId(roadmapDto.clientId())
                    .build();
        }
    }

    @Builder
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

            return ListItem.builder()
                    .id(roadmap.getId())
                    .title(roadmap.getTitle())
                    .image(roadmap.getImage())
                    .accessibility(roadmap.getAccessibility())
                    .isOfficial(roadmap.isOfficial())
                    .likes(roadmap.getLikes())
                    .createdDate(roadmap.getCreatedDate())
                    .clientId(clientId)
                    .build();
        }

        @Builder
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

                return Response.builder()
                        .id(listItem.id)
                        .title(listItem.title)
                        .image(listItem.image)
                        .accessibility(accessibility)
                        .isOfficial(listItem.isOfficial)
                        .likes(listItem.likes)
                        .createdDate(createdDate)
                        .clientId(listItem.clientId)
                        .build();
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
