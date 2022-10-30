package com.devjaewoo.openroadmaps.domain.roadmap.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoadmapItemReferenceTest {

    @Test
    @DisplayName("create")
    public void create() {
        //given
        RoadmapItem roadmapItem = RoadmapItem.create("name", "content", 0, 0, null, null, null, null);
        String url = "url";

        //when
        RoadmapItemReference roadmapItemReference = RoadmapItemReference.create(roadmapItem, url);

        //then
        assertThat(roadmapItemReference.getId()).isNull();
        assertThat(roadmapItemReference.getRoadmapItem()).isEqualTo(roadmapItem);
        assertThat(roadmapItemReference.getUrl()).isEqualTo(url);
    }

    @Test
    @DisplayName("updateRoadmapItem")
    public void updateRoadmapItem() {
        //given
        RoadmapItem roadmapItem = RoadmapItem.create("name", "content", 0, 0, null, null, null, null);
        RoadmapItemReference roadmapItemReference = RoadmapItemReference.create(null, "url");

        //when
        roadmapItemReference.updateRoadmapItem(roadmapItem);

        //then
        assertThat(roadmapItemReference.getRoadmapItem()).isEqualTo(roadmapItem);
        assertThat(roadmapItem.getReferenceList()).contains(roadmapItemReference);
    }
}