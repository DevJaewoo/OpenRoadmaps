package com.devjaewoo.openroadmaps.domain.roadmap;

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
            Recommend recommend,
            List<String> referenceList
    ) {

        public Response(RoadmapItemDto roadmapItemDto) {

            this(
                    roadmapItemDto.id,
                    roadmapItemDto.name,
                    roadmapItemDto.content,
                    roadmapItemDto.recommend,
                    roadmapItemDto.referenceList);
        }
    }

    public record ListItem(
            Long id,
            String name,
            boolean isCleared,
            Recommend recommend,
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
                    isCleared,
                    roadmapItem.getRecommend(),
                    parentId,
                    roadmapId);
        }

        public static ListItem of(RoadmapItem roadmapItem) {
            return of(roadmapItem, false);
        }

        public record Response(
                Long id,
                String name,
                boolean isCleared,
                Recommend recommend,
                Long parentId) {

            public Response(ListItem listItem) {
                this(
                        listItem.id,
                        listItem.name,
                        listItem.isCleared,
                        listItem.recommend,
                        listItem.parentId);
            }
        }
    }

    public record ResponseList(List<ListItem.Response> roadmapItemList) { }
}
