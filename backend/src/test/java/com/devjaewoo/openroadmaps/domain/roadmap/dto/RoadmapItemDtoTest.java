package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItemReference;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoadmapItemDtoTest {

    @Nested
    @DisplayName("RoadmapItemDTO")
    class Single {

        @Test
        @DisplayName("create")
        public void of() {
            // given
            Roadmap roadmap = Roadmap.create("title", "image", Accessibility.PRIVATE, null);
            roadmap.setId(1L);

            RoadmapItem roadmapItem = RoadmapItem.create("name", "content", 1, 2, Recommend.RECOMMEND, ConnectionType.b2b, null, roadmap);
            roadmapItem.setId(1L);

            RoadmapItemReference reference1 = RoadmapItemReference.create(roadmapItem, "url1");
            RoadmapItemReference reference2 = RoadmapItemReference.create(roadmapItem, "url1");

            // when
            RoadmapItemDto roadmapItemDto = RoadmapItemDto.of(roadmapItem);

            // then
            assertThat(roadmapItemDto.id()).isEqualTo(roadmapItem.getId());
            assertThat(roadmapItemDto.name()).isEqualTo(roadmapItem.getName());
            assertThat(roadmapItemDto.content()).isEqualTo(roadmapItem.getContent());
            assertThat(roadmapItemDto.recommend()).isEqualTo(roadmapItem.getRecommend());
            assertThat(roadmapItemDto.referenceList()).contains(reference1.getUrl(), reference2.getUrl());
            assertThat(roadmapItemDto.roadmapId()).isEqualTo(roadmap.getId());
        }

        @Test
        @DisplayName("Response")
        public void response() {
            // given
            RoadmapItem roadmapItem = RoadmapItem.create("name", "content", 1, 2, Recommend.RECOMMEND, ConnectionType.b2b, null, null);
            roadmapItem.setId(1L);

            RoadmapItemReference reference1 = RoadmapItemReference.create(roadmapItem, "url1");
            RoadmapItemReference reference2 = RoadmapItemReference.create(roadmapItem, "url1");

            RoadmapItemDto roadmapItemDto = RoadmapItemDto.of(roadmapItem);

            // when
            RoadmapItemDto.Response response = RoadmapItemDto.Response.of(roadmapItemDto);

            // then
            assertThat(response.id()).isEqualTo(roadmapItem.getId());
            assertThat(response.name()).isEqualTo(roadmapItem.getName());
            assertThat(response.content()).isEqualTo(roadmapItem.getContent());
            assertThat(response.recommend()).isEqualTo(roadmapItem.getRecommend().name());
            assertThat(response.referenceList()).contains(reference1.getUrl(), reference2.getUrl());
        }
    }

    @Nested
    @DisplayName("ListItem")
    class Multiple {

        @Test
        @DisplayName("create")
        public void of() {
            // given
            Roadmap roadmap = Roadmap.create("title", "image", Accessibility.PRIVATE, null);
            roadmap.setId(1L);

            RoadmapItem parent = RoadmapItem.create("parent", "parentContent", 1, 2, null, null, null, roadmap);
            parent.setId(1L);

            RoadmapItem roadmapItem = RoadmapItem.create("name", "content", 3, 4, Recommend.RECOMMEND, ConnectionType.b2b, parent, roadmap);
            roadmapItem.setId(2L);

            // when
            RoadmapItemDto.ListItem listItem = RoadmapItemDto.ListItem.of(roadmapItem);

            // then
            assertThat(listItem.id()).isEqualTo(roadmapItem.getId());
            assertThat(listItem.name()).isEqualTo(roadmapItem.getName());
            assertThat(listItem.x()).isEqualTo(roadmapItem.getX());
            assertThat(listItem.y()).isEqualTo(roadmapItem.getY());
            assertThat(listItem.recommend()).isEqualTo(roadmapItem.getRecommend());
            assertThat(listItem.connectionType()).isEqualTo(roadmapItem.getConnectionType());
            assertThat(listItem.isCleared()).isFalse(); // Default false
            assertThat(listItem.parentId()).isEqualTo(parent.getId());
            assertThat(listItem.roadmapId()).isEqualTo(roadmap.getId());
        }

        @Test
        @DisplayName("Response")
        public void response() {
            // given
            RoadmapItem parent = RoadmapItem.create("parent", "parentContent", 1, 2, null, null, null, null);
            parent.setId(1L);

            RoadmapItem roadmapItem = RoadmapItem.create("name", "content", 3, 4, Recommend.RECOMMEND, ConnectionType.b2b, parent, null);
            roadmapItem.setId(2L);

            RoadmapItemDto.ListItem listItem = RoadmapItemDto.ListItem.of(roadmapItem);

            // when
            RoadmapItemDto.ListItem.Response response = RoadmapItemDto.ListItem.Response.of(listItem);

            // then
            assertThat(response.id()).isEqualTo(roadmapItem.getId());
            assertThat(response.name()).isEqualTo(roadmapItem.getName());
            assertThat(response.x()).isEqualTo(roadmapItem.getX());
            assertThat(response.y()).isEqualTo(roadmapItem.getY());
            assertThat(response.recommend()).isEqualTo(roadmapItem.getRecommend().name());
            assertThat(response.connectionType()).isEqualTo(roadmapItem.getConnectionType().name());
            assertThat(response.isCleared()).isEqualTo(listItem.isCleared());
            assertThat(response.parentId()).isEqualTo(parent.getId());
        }
    }
}