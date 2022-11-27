package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RoadmapLikeDtoTest {

    @Test
    @DisplayName("create")
    public void create() {
        // given
        Client client = Client.create("name", "email", "password");
        Roadmap roadmap = Roadmap.create("title", "image", null, null);
        RoadmapLike roadmapLike = RoadmapLike.create(roadmap, client);

        // when
        RoadmapLikeDto roadmapLikeDto = RoadmapLikeDto.from(roadmapLike, roadmap.getLikes());

        // then
        assertThat(roadmapLikeDto.roadmapId()).isEqualTo(roadmap.getId());
        assertThat(roadmapLikeDto.clientId()).isEqualTo(client.getId());
        assertThat(roadmapLikeDto.liked()).isFalse();
    }

    @Test
    @DisplayName("Response")
    public void response() {
        // given
        Client client = Client.create("name", "email", "password");
        Roadmap roadmap = Roadmap.create("title", "image", null, null);
        RoadmapLike roadmapLike = RoadmapLike.create(roadmap, client);
        RoadmapLikeDto roadmapLikeDto = RoadmapLikeDto.from(roadmapLike, roadmap.getLikes());

        // when
        RoadmapLikeDto.LikeResponse response = RoadmapLikeDto.LikeResponse.from(roadmapLikeDto);

        // then
        assertThat(response.roadmapId()).isEqualTo(roadmap.getId());
        assertThat(response.liked()).isFalse();
    }
}