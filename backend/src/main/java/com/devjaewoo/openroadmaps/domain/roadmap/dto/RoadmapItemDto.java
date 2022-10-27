package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItemReference;

import java.util.List;

public record RoadmapItemDto(
        Long id,
        String name,
        String content,
        Recommend recommend,
        List<String> referenceList,
        Long roadmapId
) {

    public static RoadmapItemDto of(RoadmapItem roadmapItem) {

        List<String> referenceList = roadmapItem.getReferenceList().stream()
                .map(RoadmapItemReference::getUrl)
                .toList();

        Long roadmapId = null;
        if(roadmapItem.getRoadmap() != null) {
            roadmapId = roadmapItem.getRoadmap().getId();
        }

        return new RoadmapItemDto(
                roadmapItem.getId(),
                roadmapItem.getName(),
                roadmapItem.getContent(),
                roadmapItem.getRecommend(),
                referenceList,
                roadmapId);
    }

    public record Response(
            Long id,
            String name,
            String content,
            String recommend,
            List<String> referenceList
    ) {

        public static Response of(RoadmapItemDto roadmapItemDto) {

            String recommend = null;
            if(roadmapItemDto.recommend != null) {
                recommend = roadmapItemDto.recommend.name();
            }

            return new Response(
                    roadmapItemDto.id,
                    roadmapItemDto.name,
                    roadmapItemDto.content,
                    recommend,
                    roadmapItemDto.referenceList);
        }
    }

    public record ListItem(
            Long id,
            String name,
            double x,
            double y,
            Recommend recommend,
            ConnectionType connectionType,
            boolean isCleared,
            Long parentId,
            Long roadmapId) {

        public static ListItem of(RoadmapItem roadmapItem, boolean isCleared) {

            Long parentId = null;
            if(roadmapItem.getParent() != null) {
                parentId = roadmapItem.getParent().getId();
            }

            Long roadmapId = null;
            if(roadmapItem.getRoadmap() != null) {
                roadmapId = roadmapItem.getRoadmap().getId();
            }

            return new ListItem(
                    roadmapItem.getId(),
                    roadmapItem.getName(),
                    roadmapItem.getX(),
                    roadmapItem.getY(),
                    roadmapItem.getRecommend(),
                    roadmapItem.getConnectionType(),
                    isCleared,
                    parentId,
                    roadmapId);
        }

        public static ListItem of(RoadmapItem roadmapItem) {
            return of(roadmapItem, false);
        }

        public record Response(
                Long id,
                String name,
                double x,
                double y,
                String recommend,
                String connectionType,
                boolean isCleared,
                Long parentId) {

            public static Response of(ListItem listItem) {

                String recommend = null;
                if(listItem.recommend != null) {
                    recommend = listItem.recommend.name();
                }

                String connectionType = null;
                if(listItem.connectionType != null) {
                    connectionType = listItem.connectionType.name();
                }

                return new Response(
                        listItem.id,
                        listItem.name,
                        listItem.x,
                        listItem.y,
                        recommend,
                        connectionType,
                        listItem.isCleared,
                        listItem.parentId);
            }
        }
    }

    public record ResponseList(List<ListItem.Response> roadmapItemList) { }
}
