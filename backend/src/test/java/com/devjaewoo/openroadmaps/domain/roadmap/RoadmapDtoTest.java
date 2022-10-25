package com.devjaewoo.openroadmaps.domain.roadmap;

import com.devjaewoo.openroadmaps.domain.client.Client;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoadmapDtoTest {

    @Nested
    @DisplayName("RoadmapDTO")
    class Single {

        @Test
        @DisplayName("create")
        public void of() {
            // given
            Client client = Client.create("name", "email", "password");
            client.setId(1L);

            Roadmap roadmap1 = Roadmap.create("title", "image", Accessibility.PRIVATE, null);
            Roadmap roadmap2 = Roadmap.create("title", "image", Accessibility.PRIVATE, client);
            roadmap2.setId(1L);

            RoadmapItem roadmapItem1 = RoadmapItem.create("name1", "content1", 0, 0, null, null, null, roadmap1);
            RoadmapItem roadmapItem2 = RoadmapItem.create("name2", "content2", 0, 0, null, null, null, roadmap1);

            // when
            RoadmapDto roadmapDto1 = RoadmapDto.of(roadmap1);
            RoadmapDto roadmapDto2 = RoadmapDto.of(roadmap2);

            // then
            assertThat(roadmapDto1.id()).isNull();
            assertThat(roadmapDto1.title()).isEqualTo(roadmap1.getTitle());
            assertThat(roadmapDto1.image()).isEqualTo(roadmap1.getImage());
            assertThat(roadmapDto1.accessibility()).isEqualTo(roadmap1.getAccessibility());
            assertThat(roadmapDto1.likes()).isZero();
            assertThat(roadmapDto1.clientId()).isNull();
            assertThat(roadmapDto1.roadmapItemList().get(0)).isInstanceOf(RoadmapItemDto.ListItem.class);
            assertThat(roadmapDto1.roadmapItemList()).extracting("name")
                    .contains(roadmapItem1.getName(), roadmapItem2.getName());

            assertThat(roadmapDto2.id()).isEqualTo(roadmap2.getId());
            assertThat(roadmapDto2.clientId()).isEqualTo(client.getId());
        }

        @Test
        @DisplayName("Response")
        public void response() {
            // given
            Roadmap roadmap = Roadmap.create("title", "image", Accessibility.PRIVATE, null);
            roadmap.setId(1L);

            RoadmapItem roadmapItem1 = RoadmapItem.create("name1", "content1", 0, 0, null, null, null, roadmap);
            RoadmapItem roadmapItem2 = RoadmapItem.create("name2", "content2", 0, 0, null, null, null, roadmap);

            RoadmapDto roadmapDto = RoadmapDto.of(roadmap);

            // when
            RoadmapDto.Response response = RoadmapDto.Response.of(roadmapDto);

            // then
            assertThat(response.id()).isEqualTo(roadmap.getId());
            assertThat(response.title()).isEqualTo(roadmap.getTitle());
            assertThat(response.image()).isEqualTo(roadmap.getImage());
            assertThat(response.accessibility()).isEqualTo(roadmap.getAccessibility().name());
            assertThat(response.likes()).isEqualTo(roadmap.getLikes());
            assertThat(response.roadmapItemList().get(0)).isInstanceOf(RoadmapItemDto.ListItem.Response.class);
            assertThat(response.roadmapItemList()).extracting("name")
                    .contains(roadmapItem1.getName(), roadmapItem2.getName());
        }
    }

    @Nested
    @DisplayName("ListItem")
    class Multiple {

        @Test
        @DisplayName("create")
        public void of() {
            // given
            Client client = Client.create("name", "email", "password");
            client.setId(1L);

            Roadmap roadmap = Roadmap.create("title", "image", Accessibility.PRIVATE, client);
            roadmap.setId(1L);

            // when
            RoadmapDto.ListItem listItem = RoadmapDto.ListItem.of(roadmap);

            // then
            assertThat(listItem.id()).isEqualTo(roadmap.getId());
            assertThat(listItem.title()).isEqualTo(roadmap.getTitle());
            assertThat(listItem.image()).isEqualTo(roadmap.getImage());
            assertThat(listItem.accessibility()).isEqualTo(roadmap.getAccessibility());
            assertThat(listItem.isOfficial()).isEqualTo(roadmap.isOfficial());
            assertThat(listItem.likes()).isEqualTo(roadmap.getLikes());
            assertThat(listItem.clientId()).isEqualTo(roadmap.getClient().getId());
        }

        @Test
        @DisplayName("Response")
        public void response() {
            // given
            Client client = Client.create("name", "email", "password");
            client.setId(1L);

            Roadmap roadmap = Roadmap.create("title", "image", Accessibility.PRIVATE, client);
            roadmap.setId(1L);

            RoadmapDto.ListItem listItem = RoadmapDto.ListItem.of(roadmap);

            // when
            RoadmapDto.ListItem.Response response = RoadmapDto.ListItem.Response.of(listItem);

            // then
            assertThat(response.id()).isEqualTo(roadmap.getId());
            assertThat(response.title()).isEqualTo(roadmap.getTitle());
            assertThat(response.image()).isEqualTo(roadmap.getImage());
            assertThat(response.accessibility()).isEqualTo(roadmap.getAccessibility().name());
            assertThat(response.isOfficial()).isEqualTo(roadmap.isOfficial());
            assertThat(response.likes()).isEqualTo(roadmap.getLikes());
            assertThat(response.clientId()).isEqualTo(roadmap.getClient().getId());
        }
    }
}