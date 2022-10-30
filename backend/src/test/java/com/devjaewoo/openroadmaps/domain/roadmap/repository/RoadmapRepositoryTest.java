package com.devjaewoo.openroadmaps.domain.roadmap.repository;

import com.devjaewoo.openroadmaps.RepositoryTest;
import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.domain.client.repository.ClientRepository;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapSearch;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
@Transactional
class RoadmapRepositoryTest {

    @Autowired ClientRepository clientRepository;
    @Autowired RoadmapRepository roadmapRepository;

    @Nested
    @DisplayName("Roadmap 조회")
    class search {

        @Test
        @DisplayName("ID")
        public void findById() {
            //given
            Roadmap roadmap = Roadmap.create("title", "image", null, null);
            roadmapRepository.save(roadmap);

            //when
            Roadmap result1 = roadmapRepository.findById(roadmap.getId()).orElse(null);
            Roadmap result2 = roadmapRepository.findById(0L).orElse(null);

            //then
            assertThat(result1).isNotNull();
            assertThat(result2).isNull();
        }

        //page 12개씩 조회

        @Test
        @DisplayName("RoadmapSearch")
        public void findByRoadmapSearch() {
            //given
            Client client1 = Client.create("name", "email", "password");
            Client client2 = Client.create("name", "email", "password");

            Roadmap roadmap1 = Roadmap.create("title1", "image", Accessibility.PUBLIC, client1);
            Roadmap roadmap2 = Roadmap.create("title2", "image", Accessibility.PROTECTED, client1);
            Roadmap roadmap3 = Roadmap.create("title1", "image", Accessibility.PROTECTED, client2);
            Roadmap roadmap4 = Roadmap.create("title2", "image", Accessibility.PUBLIC, client2);

            Roadmap roadmap5 = Roadmap.create("title", "image", Accessibility.PRIVATE, client1);
            Roadmap roadmap6 = Roadmap.create("title", "image", Accessibility.PUBLIC, client2);
            roadmap6.setDeleted(true);

            roadmap1.setLikes(2);
            roadmap2.setLikes(1);
            roadmap3.setLikes(4);
            roadmap4.setLikes(3);

            roadmap2.setOfficial(true);
            roadmap3.setOfficial(true);


            clientRepository.save(client1);
            clientRepository.save(client2);

            roadmapRepository.save(roadmap1);
            roadmapRepository.save(roadmap2);
            roadmapRepository.save(roadmap3);
            roadmapRepository.save(roadmap4);
            roadmapRepository.save(roadmap5);
            roadmapRepository.save(roadmap6);

            RoadmapSearch searchClient1 = new RoadmapSearch(client1.getId(), null, null, null, 0);
            RoadmapSearch searchClient2 = new RoadmapSearch(client2.getId(), null, null, null, 0);
            RoadmapSearch searchClientNone = new RoadmapSearch(999L, null, null, null, 0);

            RoadmapSearch searchTitle1 = new RoadmapSearch(null, "title1", null, null, 0);
            RoadmapSearch searchTitle2 = new RoadmapSearch(null, "title2", null, null, 0);

            RoadmapSearch searchOfficialTrue = new RoadmapSearch(null, null, true, null, 0);
            RoadmapSearch searchOfficialFalse = new RoadmapSearch(null, null, false, null, 0);

            RoadmapSearch searchOrderByCreatedDate = new RoadmapSearch(null, null, null, RoadmapSearch.Order.LATEST, 0);
            RoadmapSearch searchOrderByLike = new RoadmapSearch(null, null, null, RoadmapSearch.Order.LIKE, 0);

            //when
            PageRequest pageRequest = PageRequest.of(0, 12);
            Page<Roadmap> resultClient1 = roadmapRepository.search(searchClient1, pageRequest);
            Page<Roadmap> resultClient2 = roadmapRepository.search(searchClient2, pageRequest);
            Page<Roadmap> resultClient3 = roadmapRepository.search(searchClientNone, pageRequest);

            Page<Roadmap> resultTitle1 = roadmapRepository.search(searchTitle1, pageRequest);
            Page<Roadmap> resultTitle2 = roadmapRepository.search(searchTitle2, pageRequest);

            Page<Roadmap> resultOfficial1 = roadmapRepository.search(searchOfficialTrue, pageRequest);
            Page<Roadmap> resultOfficial2 = roadmapRepository.search(searchOfficialFalse, pageRequest);

            Page<Roadmap> resultOrder1 = roadmapRepository.search(searchOrderByCreatedDate, pageRequest);
            Page<Roadmap> resultOrder2 = roadmapRepository.search(searchOrderByLike, pageRequest);

            //then
            assertThat(roadmap1.getCreatedDate()).isNotNull();

            assertThat(resultClient1.getContent()).contains(roadmap1, roadmap2);
            assertThat(resultClient2.getContent()).contains(roadmap3, roadmap4);
            assertThat(resultClient3.getContent()).isEmpty();

            assertThat(resultTitle1.getContent()).contains(roadmap1, roadmap3);
            assertThat(resultTitle2.getContent()).contains(roadmap2, roadmap4);

            assertThat(resultOfficial1.getContent()).contains(roadmap2, roadmap3);
            assertThat(resultOfficial2.getContent()).contains(roadmap1, roadmap4);

            assertThat(resultOrder1.getContent())
                    .contains(roadmap4, Index.atIndex(0))
                    .contains(roadmap3, Index.atIndex(1))
                    .contains(roadmap2, Index.atIndex(2))
                    .contains(roadmap1, Index.atIndex(3));

            assertThat(resultOrder2.getContent())
                    .contains(roadmap3, Index.atIndex(0))
                    .contains(roadmap4, Index.atIndex(1))
                    .contains(roadmap1, Index.atIndex(2))
                    .contains(roadmap2, Index.atIndex(3));
        }
    }
}