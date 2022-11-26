package com.devjaewoo.openroadmaps.domain.roadmap.repository;

import com.devjaewoo.openroadmaps.RepositoryTest;
import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.domain.client.repository.ClientRepository;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class RoadmapLikeRepositoryTest {

    @Autowired ClientRepository clientRepository;
    @Autowired RoadmapRepository roadmapRepository;
    @Autowired RoadmapLikeRepository roadmapLikeRepository;

    @Test
    @DisplayName("단일 조회")
    public void findByRoadmapIdAndClientId() {
        // given
        Client client = Client.create("name", "example@example.com", "!@QW12qw");
        clientRepository.save(client);

        Roadmap roadmap1 = Roadmap.create("title", "image", null, client);
        Roadmap roadmap2 = Roadmap.create("title", "image", null, client);
        roadmapRepository.saveAll(List.of(roadmap1, roadmap2));

        RoadmapLike roadmapLike1 = RoadmapLike.create(roadmap1, client);
        roadmapLike1.setLike(true);

        RoadmapLike roadmapLike2 = RoadmapLike.create(roadmap2, client);
        roadmapLike2.setLike(false);

        roadmapLikeRepository.saveAll(List.of(roadmapLike1, roadmapLike2));

        // when
        RoadmapLike result1 = roadmapLikeRepository.findByRoadmapIdAndClientId(roadmap1.getId(), client.getId()).orElse(null);
        RoadmapLike result2 = roadmapLikeRepository.findByRoadmapIdAndClientId(roadmap2.getId(), client.getId()).orElse(null);
        RoadmapLike result3 = roadmapLikeRepository.findByRoadmapIdAndClientId(999L, 999L).orElse(null);

        // then
        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result3).isNull();

        assertThat(result1.isLike()).isEqualTo(roadmapLike1.isLike());
        assertThat(result2.isLike()).isEqualTo(roadmapLike2.isLike());
    }
}