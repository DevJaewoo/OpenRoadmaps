package com.devjaewoo.openroadmaps.domain.roadmap;

import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoadmapItemTest {

    @Test
    @DisplayName("create")
    public void create() {
        //given
        String name = "name";
        String content = "content";
        Recommend recommend = Recommend.RECOMMEND;
        ConnectionType connectionType = ConnectionType.B2B;

        //when
        RoadmapItem roadmapItem = RoadmapItem.create(name, content, recommend, connectionType, null, null);

        //then
        assertThat(roadmapItem.getId()).isNull();
        assertThat(roadmapItem.getName()).isEqualTo(name);
        assertThat(roadmapItem.getContent()).isEqualTo(content);
        assertThat(roadmapItem.getRecommend()).isEqualTo(recommend);
        assertThat(roadmapItem.getConnectionType()).isEqualTo(connectionType);
        assertThat(roadmapItem.getRoadmapItemList().size()).isZero();
        assertThat(roadmapItem.getReferenceList().size()).isZero();
    }

    @Test
    @DisplayName("updateRoadmap")
    public void updateRoadmap() {
        //given
        Roadmap roadmap = Roadmap.create("title", "image", Accessibility.PUBLIC, null);

        //when
        RoadmapItem roadmapItem1 = RoadmapItem.create("name", "content", Recommend.RECOMMEND, ConnectionType.B2B, null, null);
        RoadmapItem roadmapItem2 = RoadmapItem.create("name", "content", Recommend.RECOMMEND, ConnectionType.B2B, null, roadmap);
        roadmapItem1.updateRoadmap(roadmap);

        //then
        assertThat(roadmapItem1.getRoadmap()).isEqualTo(roadmap);
        assertThat(roadmapItem2.getRoadmap()).isEqualTo(roadmap);
        assertThat(roadmap.getRoadmapItemList()).contains(roadmapItem1, roadmapItem2);
    }

    @Test
    @DisplayName("updateParent")
    public void updateParent() {
        //given
        RoadmapItem parent = RoadmapItem.create("parentName", "parentContent", Recommend.NOT_RECOMMEND, ConnectionType.T2B, null, null);

        //when
        RoadmapItem roadmapItem1 = RoadmapItem.create("name", "content", Recommend.RECOMMEND, ConnectionType.B2B, null, null);
        RoadmapItem roadmapItem2 = RoadmapItem.create("name", "content", Recommend.RECOMMEND, ConnectionType.B2B, parent, null);
        roadmapItem1.updateParent(parent);

        //then
        assertThat(roadmapItem1.getParent()).isEqualTo(parent);
        assertThat(roadmapItem2.getParent()).isEqualTo(parent);
        assertThat(parent.getRoadmapItemList()).contains(roadmapItem1, roadmapItem2);
    }

    @Test
    @DisplayName("addRoadmapItem")
    public void addRoadmapItem() {
        //given
        RoadmapItem parent = RoadmapItem.create("parentName", "parentContent", Recommend.NOT_RECOMMEND, ConnectionType.T2B, null, null);
        RoadmapItem roadmapItem = RoadmapItem.create("name", "content", Recommend.RECOMMEND, ConnectionType.B2B, null, null);

        //when
        parent.addRoadmapItem(roadmapItem);

        //then
        assertThat(roadmapItem.getParent()).isEqualTo(parent);
        assertThat(parent.getRoadmapItemList()).contains(roadmapItem);
    }

    @Test
    @DisplayName("addReference")
    public void addReference() {
        //given
        RoadmapItem roadmapItem = RoadmapItem.create("name", "content", Recommend.RECOMMEND, ConnectionType.B2B, null, null);
        RoadmapItemReference reference = RoadmapItemReference.create(null, "URL");

        //when
        roadmapItem.addReference(reference);

        //then
        assertThat(reference.getRoadmapItem()).isEqualTo(roadmapItem);
        assertThat(roadmapItem.getReferenceList()).contains(reference);
    }
}