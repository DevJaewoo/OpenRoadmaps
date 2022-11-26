package com.devjaewoo.openroadmaps.domain.roadmap.repository;

import com.devjaewoo.openroadmaps.RepositoryTest;
import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.domain.client.repository.ClientRepository;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItemClear;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RepositoryTest
class RoadmapItemClearRepositoryTest {

    @Autowired ClientRepository clientRepository;
    @Autowired RoadmapRepository roadmapRepository;
    @Autowired RoadmapItemClearRepository roadmapItemClearRepository;

    @Test
    @DisplayName("단일 조회")
    public void findByRoadmapItemIdAndClientId() {
        // given
        Client client = Client.create("name", "example@example.com", "!@QW12qw");
        clientRepository.save(client);

        Roadmap roadmap = Roadmap.create("title", "image", null, client);
        RoadmapItem roadmapItem1 = RoadmapItem.create("item1", "item1", 1, 1, null, null, null, roadmap);
        RoadmapItem roadmapItem2 = RoadmapItem.create("item2", "item2", 1, 1, null, null, null, roadmap);
        roadmapRepository.save(roadmap);

        RoadmapItemClear roadmapItemClear1 = RoadmapItemClear.create(roadmapItem1, client);
        roadmapItemClear1.setCleared(false);

        RoadmapItemClear roadmapItemClear2 = RoadmapItemClear.create(roadmapItem2, client);
        roadmapItemClear2.setCleared(true);

        roadmapItemClearRepository.saveAll(List.of(roadmapItemClear1, roadmapItemClear2));

        // when
        RoadmapItemClear result1 = roadmapItemClearRepository.findByRoadmapItemIdAndClientId(roadmapItem1.getId(), client.getId()).orElse(null);
        RoadmapItemClear result2 = roadmapItemClearRepository.findByRoadmapItemIdAndClientId(roadmapItem2.getId(), client.getId()).orElse(null);
        RoadmapItemClear result3 = roadmapItemClearRepository.findByRoadmapItemIdAndClientId(999L, 999L).orElse(null);

        // then
        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3).isNull();

        assertThat(result1.isCleared()).isEqualTo(roadmapItemClear1.isCleared());
        assertThat(result2.isCleared()).isEqualTo(roadmapItemClear2.isCleared());
    }

    @Test
    @DisplayName("로드맵 목록 조회")
    public void findAllByRoadmapItemInAndClientId() {
        // given
        Client client = Client.create("name", "example@example.com", "!@QW12qw");
        clientRepository.save(client);

        Roadmap roadmap = Roadmap.create("title", "image", null, client);
        RoadmapItem roadmapItem1 = RoadmapItem.create("item1", "item1", 1, 1, null, null, null, roadmap);
        RoadmapItem roadmapItem2 = RoadmapItem.create("item2", "item2", 1, 1, null, null, null, roadmap);
        roadmapRepository.save(roadmap);

        RoadmapItemClear roadmapItemClear1 = RoadmapItemClear.create(roadmapItem1, client);
        RoadmapItemClear roadmapItemClear2 = RoadmapItemClear.create(roadmapItem2, client);
        roadmapItemClearRepository.saveAll(List.of(roadmapItemClear1, roadmapItemClear2));

        // when
        List<RoadmapItemClear> result = roadmapItemClearRepository.findAllByRoadmapItemInAndClientId(roadmap.getRoadmapItemList(), client.getId());

        // then
        assertThat(result.size()).isEqualTo(2);
    }
}