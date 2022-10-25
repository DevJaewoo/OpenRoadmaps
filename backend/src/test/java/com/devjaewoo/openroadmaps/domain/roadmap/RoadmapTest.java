package com.devjaewoo.openroadmaps.domain.roadmap;

import com.devjaewoo.openroadmaps.domain.client.Client;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoadmapTest {

    @Test
    @DisplayName("create")
    public void create() {
        //given
        String title = "title";
        String image = "image";
        Accessibility accessibility = Accessibility.PRIVATE;
        Client client = Client.create("name", "email", "password");

        //when
        Roadmap roadmap = Roadmap.create(title, image, accessibility, client);

        //then
        assertThat(roadmap.getId()).isNull();
        assertThat(roadmap.getTitle()).isEqualTo(title);
        assertThat(roadmap.getImage()).isEqualTo(image);
        assertThat(roadmap.getAccessibility()).isEqualTo(accessibility);
        assertThat(roadmap.isOfficial()).isFalse();
        assertThat(roadmap.getLikes()).isZero();
        assertThat(roadmap.getRoadmapItemList().size()).isZero();
        assertThat(roadmap.getClient().getName()).isEqualTo(client.getName());
    }

    @Test
    @DisplayName("addRoadmapItem")
    public void addRoadmapItem() {
        //given
        Roadmap roadmap = Roadmap.create("title", "image", Accessibility.PRIVATE, null);
        RoadmapItem roadmapItem = RoadmapItem.create("name", "content", null, null, null, null);

        //when
        roadmap.addRoadmapItem(roadmapItem);

        //then
        assertThat(roadmap.getRoadmapItemList()).contains(roadmapItem);
        assertThat(roadmapItem.getRoadmap()).isEqualTo(roadmap);
    }
}