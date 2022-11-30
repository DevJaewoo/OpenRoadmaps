package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItemReference;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
public record RoadmapItemDto(
        Long id,
        String name,
        String content,
        Recommend recommend,
        List<String> referenceList,
        Long roadmapId
) {

    public static RoadmapItemDto from(RoadmapItem roadmapItem) {

        List<String> referenceList = roadmapItem.getReferenceList().stream()
                .map(RoadmapItemReference::getUrl)
                .toList();

        Long roadmapId = null;
        if(roadmapItem.getRoadmap() != null) {
            roadmapId = roadmapItem.getRoadmap().getId();
        }

        return RoadmapItemDto.builder()
                .id(roadmapItem.getId())
                .name(roadmapItem.getName())
                .content(roadmapItem.getContent())
                .recommend(roadmapItem.getRecommend())
                .referenceList(referenceList)
                .roadmapId(roadmapId)
                .build();
    }

    @Builder
    public record Response(
            Long id,
            String name,
            String content,
            String recommend,
            List<String> referenceList
    ) {

        public static Response from(RoadmapItemDto roadmapItemDto) {

            String recommend = null;
            if(roadmapItemDto.recommend != null) {
                recommend = roadmapItemDto.recommend.name();
            }

            return Response.builder()
                    .id(roadmapItemDto.id)
                    .name(roadmapItemDto.name)
                    .content(roadmapItemDto.content)
                    .recommend(recommend)
                    .referenceList(roadmapItemDto.referenceList)
                    .build();
        }
    }

    @Builder
    public record ListItem(
            Long id,
            String name,
            String content,
            double x,
            double y,
            Recommend recommend,
            ConnectionType connectionType,
            boolean isCleared,
            Long parentId,
            List<String> referenceList,
            Long roadmapId) {

        public static ListItem from(RoadmapItem roadmapItem, boolean isCleared) {

            Long parentId = null;
            if(roadmapItem.getParent() != null) {
                parentId = roadmapItem.getParent().getId();
            }

            Long roadmapId = null;
            if(roadmapItem.getRoadmap() != null) {
                roadmapId = roadmapItem.getRoadmap().getId();
            }

            return ListItem.builder()
                    .id(roadmapItem.getId())
                    .name(roadmapItem.getName())
                    .content(roadmapItem.getContent())
                    .x(roadmapItem.getX())
                    .y(roadmapItem.getY())
                    .recommend(roadmapItem.getRecommend())
                    .connectionType(roadmapItem.getConnectionType())
                    .isCleared(isCleared)
                    .parentId(parentId)
                    .referenceList(
                            roadmapItem.getReferenceList().stream()
                                    .map(RoadmapItemReference::getUrl)
                                    .toList()
                    )
                    .roadmapId(roadmapId)
                    .build();
        }

        public static ListItem from(RoadmapItem roadmapItem) {
            return from(roadmapItem, false);
        }

        @Builder
        public record Response(
                Long id,
                String name,
                String content,
                double x,
                double y,
                String recommend,
                String connectionType,
                boolean isCleared,
                Long parentId,
                List<String> referenceList) {

            public static Response from(ListItem listItem) {

                String recommend = null;
                if(listItem.recommend != null) {
                    recommend = listItem.recommend.name();
                }

                String connectionType = null;
                if(listItem.connectionType != null) {
                    connectionType = listItem.connectionType.name();
                }

                return Response.builder()
                        .id(listItem.id)
                        .name(listItem.name)
                        .content(listItem.content)
                        .x(listItem.x)
                        .y(listItem.y)
                        .recommend(recommend)
                        .connectionType(connectionType)
                        .isCleared(listItem.isCleared)
                        .parentId(listItem.parentId)
                        .referenceList(listItem.referenceList)
                        .build();
            }
        }
    }

    public record ResponseList(List<ListItem.Response> roadmapItemList) { }

    public record CreateRequest(
            @NotNull Long id,

            @NotNull
            @Size(min = 1, max = 50)
            String name,

            @Size(max = 500)
            String content,

            @NotNull double x,
            @NotNull double y,
            Recommend recommend,
            ConnectionType connectionType,
            @Size(max = 10)
            List<String> referenceList,
            Long parentId) {

        public RoadmapItem toEntity() {
            RoadmapItem roadmapItem = RoadmapItem.create(name, content, x, y, recommend, connectionType, null, null);

            if(referenceList != null) {
                List<RoadmapItemReference> referenceList = this.referenceList.stream()
                        .map(r -> RoadmapItemReference.create(roadmapItem, r))
                        .toList();

                roadmapItem.setReferenceList(referenceList);
            }

            return roadmapItem;
        }
    }
}
