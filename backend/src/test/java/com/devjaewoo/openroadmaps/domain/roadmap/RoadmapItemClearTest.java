package com.devjaewoo.openroadmaps.domain.roadmap;

import com.devjaewoo.openroadmaps.domain.client.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoadmapItemClearTest {

    @Test
    @DisplayName("create")
    public void create() {
        //given
        Client client = Client.create("name", "email", "password");
        RoadmapItem roadmapItem = RoadmapItem.create("name", "content", null, null, null, null);

        //when
        RoadmapItemClear roadmapItemClear = RoadmapItemClear.create(roadmapItem, client);

        //then
        assertThat(roadmapItemClear.getId()).isNull();
        assertThat(roadmapItemClear.getRoadmapItem()).isEqualTo(roadmapItem);
        assertThat(roadmapItemClear.getClient()).isEqualTo(client);
    }
}