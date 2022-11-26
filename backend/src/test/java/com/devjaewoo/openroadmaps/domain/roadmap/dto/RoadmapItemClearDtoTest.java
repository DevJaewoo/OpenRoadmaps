package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItemClear;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RoadmapItemClearDtoTest {

    @Test
    @DisplayName("Create")
    public void create() {
        // given
        Client client = Client.create("name", "email", "password");
        RoadmapItem roadmapItem = RoadmapItem.create("name", "content", 1, 1, null, null, null, null);
        RoadmapItemClear roadmapItemClear = RoadmapItemClear.create(roadmapItem, client);

        // when
        RoadmapItemClearDto roadmapItemClearDto = RoadmapItemClearDto.from(roadmapItemClear);

        // then
        assertThat(roadmapItemClearDto.roadmapItemId()).isEqualTo(roadmapItem.getId());
        assertThat(roadmapItemClearDto.clientId()).isEqualTo(client.getId());
        assertThat(roadmapItemClearDto.isCleared()).isTrue();
    }

    @Test
    @DisplayName("Response")
    public void response() {
        // given
        Client client = Client.create("name", "email", "password");
        RoadmapItem roadmapItem = RoadmapItem.create("name", "content", 1, 1, null, null, null, null);
        RoadmapItemClear roadmapItemClear = RoadmapItemClear.create(roadmapItem, client);
        RoadmapItemClearDto roadmapItemClearDto = RoadmapItemClearDto.from(roadmapItemClear);

        // when
        RoadmapItemClearDto.ClearResponse response = RoadmapItemClearDto.ClearResponse.from(roadmapItemClearDto);

        // then
        assertThat(response.isCleared()).isTrue();
    }
}