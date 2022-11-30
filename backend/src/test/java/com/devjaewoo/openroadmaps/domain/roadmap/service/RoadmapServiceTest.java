package com.devjaewoo.openroadmaps.domain.roadmap.service;

import com.devjaewoo.openroadmaps.domain.client.dto.ClientErrorCode;
import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.domain.client.repository.ClientRepository;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.*;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItemClear;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapLike;
import com.devjaewoo.openroadmaps.domain.roadmap.repository.RoadmapItemClearRepository;
import com.devjaewoo.openroadmaps.domain.roadmap.repository.RoadmapItemRepository;
import com.devjaewoo.openroadmaps.domain.roadmap.repository.RoadmapLikeRepository;
import com.devjaewoo.openroadmaps.domain.roadmap.repository.RoadmapRepository;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import com.devjaewoo.openroadmaps.global.exception.CommonErrorCode;
import com.devjaewoo.openroadmaps.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RoadmapServiceTest {

    @Mock ClientRepository clientRepository;
    @Mock RoadmapRepository roadmapRepository;
    @Mock RoadmapItemRepository roadmapItemRepository;
    @Mock RoadmapLikeRepository roadmapLikeRepository;
    @Mock RoadmapItemClearRepository roadmapItemClearRepository;

    @InjectMocks
    RoadmapService roadmapService;

    @Nested
    @DisplayName("ID 조회")
    class FindById {

        @Test
        @DisplayName("성공")
        public void success() {
            //given
            Roadmap roadmap = Roadmap.create("title", "image", Accessibility.PUBLIC, null);
            roadmap.setId(1L);

            given(roadmapRepository.findById(roadmap.getId())).willReturn(Optional.of(roadmap));

            //when
            RoadmapDto result = roadmapService.findById(roadmap.getId(), null);

            //then
            assertThat(result.id()).isEqualTo(roadmap.getId());
        }

        @Test
        @DisplayName("존재하지 않는 로드맵 조회")
        public void findNotExistingRoadmap() {
            //given
            given(roadmapRepository.findById(any())).willReturn(Optional.empty());

            //when
            Executable executable = () -> roadmapService.findById(1L, null);

            //then
            assertThrows(RestApiException.class, executable, CommonErrorCode.RESOURCE_NOT_FOUND.message);
        }

        @Test
        @DisplayName("Private 로드맵 접근 성공")
        public void findPrivateRoadmapSuccess() {
            //given
            Client client = Client.create("name", "email", "password");
            client.setId(1L);

            Roadmap roadmap = Roadmap.create("title", "image", Accessibility.PRIVATE, client);
            roadmap.setId(1L);

            given(roadmapRepository.findById(roadmap.getId())).willReturn(Optional.of(roadmap));

            //when
            RoadmapDto result = roadmapService.findById(roadmap.getId(), client.getId());

            //then
            assertThat(result.id()).isEqualTo(roadmap.getId());
        }

        @Test
        @DisplayName("인증 없이 Private 로드맵 접근")
        public void findPrivateRoadmapUnauthorized() {
            //given
            Roadmap roadmap = Roadmap.create("title", "image", Accessibility.PRIVATE, null);
            roadmap.setId(1L);

            given(roadmapRepository.findById(roadmap.getId())).willReturn(Optional.of(roadmap));

            //when
            Executable executable = () -> roadmapService.findById(roadmap.getId(), null);

            //then
            assertThrows(RestApiException.class, executable, CommonErrorCode.UNAUTHORIZED.message);
        }

        @Test
        @DisplayName("권한 없이 Private 로드맵 접근")
        public void findPrivateRoadmapForbidden() {
            //given
            Client client = Client.create("name", "email", "password");
            client.setId(1L);

            Roadmap roadmap = Roadmap.create("title", "image", Accessibility.PRIVATE, client);
            roadmap.setId(1L);

            given(roadmapRepository.findById(roadmap.getId())).willReturn(Optional.of(roadmap));

            //when
            Executable executable = () -> roadmapService.findById(roadmap.getId(), 999L);

            //then
            assertThrows(RestApiException.class, executable, CommonErrorCode.FORBIDDEN.message);
        }
    }

    @Nested
    @DisplayName("로드맵 생성")
    class Create {

        @Test
        @DisplayName("성공")
        public void success() {
            //given
            RoadmapItemDto.CreateRequest node1 = new RoadmapItemDto.CreateRequest(1L, "node1", "content", 0, 0, null, null, null, null);
            RoadmapItemDto.CreateRequest node2 = new RoadmapItemDto.CreateRequest(2L, "node1", "content", 0, 0, null, ConnectionType.b2t, null, 1L);
            RoadmapItemDto.CreateRequest node3 = new RoadmapItemDto.CreateRequest(3L, "node1", "content", 0, 0, null, ConnectionType.b2t, null, 1L);
            RoadmapItemDto.CreateRequest node4 = new RoadmapItemDto.CreateRequest(4L, "node1", "content", 0, 0, null, ConnectionType.b2t, null, 2L);
            RoadmapItemDto.CreateRequest node5 = new RoadmapItemDto.CreateRequest(5L, "node1", "content", 0, 0, null, ConnectionType.b2t, null, 3L);

            RoadmapDto.CreateRequest createRequest = new RoadmapDto.CreateRequest("title", "image", Accessibility.PUBLIC, List.of(node1, node2, node3, node4, node5));

            Client client = Client.create("name", "email", "password");
            client.setId(1L);

            Roadmap roadmap = Roadmap.create("title", "image", null, client);
            roadmap.setId(1L);

            given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));
            given(roadmapRepository.save(any())).willReturn(roadmap);

            //when
            Long resultId = roadmapService.create(createRequest, client.getId());

            //then
            assertThat(resultId).isEqualTo(roadmap.getId());
        }

        @Test
        @DisplayName("존재하지 않는 Client")
        public void invalidClient() {
            //given
            RoadmapDto.CreateRequest createRequest = new RoadmapDto.CreateRequest("title", "image", Accessibility.PUBLIC, new ArrayList<>());
            given(clientRepository.findById(any())).willReturn(Optional.empty());

            //when
            Executable executable = () -> roadmapService.create(createRequest, 999L);

            //then
            assertThrows(RestApiException.class, executable, ClientErrorCode.CLIENT_NOT_FOUND.message);
        }

        @Test
        @DisplayName("잘못된 부모 ID")
        public void invalidParent() {
            //given
            RoadmapItemDto.CreateRequest node1 = new RoadmapItemDto.CreateRequest(1L, "node1", "content", 0, 0, null, null, null, null);
            RoadmapItemDto.CreateRequest node2 = new RoadmapItemDto.CreateRequest(2L, "node1", "content", 0, 0, null, ConnectionType.b2t, null, 6L);

            RoadmapDto.CreateRequest createRequest = new RoadmapDto.CreateRequest("title", "image", Accessibility.PUBLIC, List.of(node1, node2));

            Client client = Client.create("name", "email", "password");
            client.setId(1L);

            given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));

            //when
            Executable executable = () -> roadmapService.create(createRequest, client.getId());

            //then
            assertThrows(RestApiException.class, executable, RoadmapErrorCode.INVALID_PARENT.message);
        }

        @Test
        @DisplayName("연결선 없이 부모 지정")
        public void invalidConnection() {
            //given
            RoadmapItemDto.CreateRequest node1 = new RoadmapItemDto.CreateRequest(1L, "node1", "content", 0, 0, null, null, null, null);
            RoadmapItemDto.CreateRequest node2 = new RoadmapItemDto.CreateRequest(2L, "node1", "content", 0, 0, null, null, null, 1L);

            RoadmapDto.CreateRequest createRequest = new RoadmapDto.CreateRequest("title", "image", Accessibility.PUBLIC, List.of(node1, node2));

            Client client = Client.create("name", "email", "password");
            client.setId(1L);

            given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));

            //when
            Executable executable = () -> roadmapService.create(createRequest, client.getId());

            //then
            assertThrows(RestApiException.class, executable, RoadmapErrorCode.INVALID_CONNECTION.message);
        }
    }

    @Nested
    @DisplayName("로드맵 항목 완료")
    class ItemClear {

        @Test
        @DisplayName("성공")
        public void success() {
            //given
            Client client = Client.create("name", "email", "password");
            client.setId(1L);
            given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));

            Roadmap roadmap = Roadmap.create("title", "image", null, client);
            roadmap.setId(2L);

            RoadmapItem roadmapItem = RoadmapItem.create("item", "content", 1, 1, null, null, null, roadmap);
            roadmapItem.setId(3L);
            given(roadmapItemRepository.findById(roadmapItem.getId())).willReturn(Optional.of(roadmapItem));

            given(roadmapItemClearRepository.findByRoadmapItemIdAndClientId(any(), any())).willReturn(Optional.empty());

            boolean cleared = true;

            //when
            RoadmapItemClearDto roadmapItemClearDto = roadmapService.clearRoadmapItem(roadmap.getId(), roadmapItem.getId(), cleared, client.getId());

            //then
            assertThat(roadmapItemClearDto.roadmapItemId()).isEqualTo(roadmapItem.getId());
            assertThat(roadmapItemClearDto.clientId()).isEqualTo(client.getId());
            assertThat(roadmapItemClearDto.isCleared()).isEqualTo(cleared);
        }

        @Test
        @DisplayName("이미 존재하는 경우 Update")
        public void update() {
            //given
            Client client = Client.create("name", "email", "password");
            client.setId(1L);
            given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));

            Roadmap roadmap = Roadmap.create("title", "image", null, client);
            roadmap.setId(2L);

            RoadmapItem roadmapItem = RoadmapItem.create("item", "content", 1, 1, null, null, null, roadmap);
            roadmapItem.setId(3L);
            given(roadmapItemRepository.findById(roadmapItem.getId())).willReturn(Optional.of(roadmapItem));

            RoadmapItemClear roadmapItemClear = RoadmapItemClear.create(roadmapItem, client);
            given(roadmapItemClearRepository.findByRoadmapItemIdAndClientId(roadmapItem.getId(), client.getId()))
                    .willReturn(Optional.of(roadmapItemClear));

            boolean cleared = false;

            //when
            RoadmapItemClearDto roadmapItemClearDto = roadmapService.clearRoadmapItem(roadmap.getId(), roadmapItem.getId(), cleared, client.getId());

            //then
            assertThat(roadmapItemClearDto.roadmapItemId()).isEqualTo(roadmapItem.getId());
            assertThat(roadmapItemClearDto.clientId()).isEqualTo(client.getId());
            assertThat(roadmapItemClearDto.isCleared()).isEqualTo(cleared);
        }

        @Test
        @DisplayName("Client가 없는 경우")
        public void clientNotFound() {
            //given
            given(clientRepository.findById(any())).willReturn(Optional.empty());

            //when
            Executable executable = () -> roadmapService.clearRoadmapItem(1L, 1L, true, 1L);

            //then
            assertThrows(RestApiException.class, executable, ClientErrorCode.CLIENT_NOT_FOUND.message);
        }

        @Test
        @DisplayName("RoadmapItem이 없는 경우")
        public void roadmapItemNotFound() {
            //given
            Client client = Client.create("name", "email", "password");
            client.setId(1L);
            given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));

            given(roadmapItemRepository.findById(any())).willReturn(Optional.empty());

            //when
            Executable executable = () -> roadmapService.clearRoadmapItem(1L, 1L, true, client.getId());

            //then
            assertThrows(RestApiException.class, executable, CommonErrorCode.RESOURCE_NOT_FOUND.message);
        }

        @Test
        @DisplayName("Roadmap에 포함되지 않는 RoadmapItem일 경우")
        public void invalidRoadmapItem() {
            //given
            Client client = Client.create("name", "email", "password");
            client.setId(1L);
            given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));

            Roadmap roadmap = Roadmap.create("title", "image", null, client);
            roadmap.setId(2L);

            RoadmapItem roadmapItem = RoadmapItem.create("item", "content", 1, 1, null, null, null, roadmap);
            roadmapItem.setId(3L);
            given(roadmapItemRepository.findById(roadmapItem.getId())).willReturn(Optional.of(roadmapItem));

            //when
            Executable executable = () -> roadmapService.clearRoadmapItem(999L, roadmapItem.getId(), true, client.getId());

            //then
            assertThrows(RestApiException.class, executable, RoadmapErrorCode.INVALID_CLEAR_ROADMAP.message);
        }
    }

    @Nested
    @DisplayName("좋아요")
    class Like {

        @Test
        @DisplayName("성공")
        public void success() {
            //given
            Client client = Client.create("name", "email", "password");
            client.setId(1L);
            given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));

            Roadmap roadmap = Roadmap.create("title", "image", null, client);
            roadmap.setId(2L);
            given(roadmapRepository.findById(roadmap.getId())).willReturn(Optional.of(roadmap));

            given(roadmapLikeRepository.findByRoadmapIdAndClientId(any(), any())).willReturn(Optional.empty());

            boolean like = true;

            //when
            RoadmapLikeDto roadmapLikeDto = roadmapService.likeRoadmap(roadmap.getId(), like, client.getId());

            //then
            assertThat(roadmapLikeDto.roadmapId()).isEqualTo(roadmap.getId());
            assertThat(roadmapLikeDto.clientId()).isEqualTo(client.getId());
            assertThat(roadmapLikeDto.liked()).isEqualTo(like);
            assertThat(roadmap.getLikes()).isEqualTo(1);
        }

        @Test
        @DisplayName("이미 존재하는 경우 Update")
        public void update() {
            //given
            Client client = Client.create("name", "email", "password");
            client.setId(1L);
            given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));

            Roadmap roadmap = Roadmap.create("title", "image", null, client);
            roadmap.setId(2L);
            given(roadmapRepository.findById(roadmap.getId())).willReturn(Optional.of(roadmap));

            RoadmapLike roadmapLike = RoadmapLike.create(roadmap, client);
            given(roadmapLikeRepository.findByRoadmapIdAndClientId(roadmap.getId(), client.getId()))
                    .willReturn(Optional.of(roadmapLike));

            boolean like = true;

            //when
            RoadmapLikeDto roadmapLikeDto = roadmapService.likeRoadmap(roadmap.getId(), like, client.getId());

            //then
            assertThat(roadmapLikeDto.roadmapId()).isEqualTo(roadmap.getId());
            assertThat(roadmapLikeDto.clientId()).isEqualTo(client.getId());
            assertThat(roadmapLikeDto.liked()).isEqualTo(like);
        }

        @Test
        @DisplayName("Client가 없는 경우")
        public void clientNotFound() {
            //given
            given(clientRepository.findById(any())).willReturn(Optional.empty());

            //when
            Executable executable = () -> roadmapService.likeRoadmap(1L, true, 1L);

            //then
            assertThrows(RestApiException.class, executable, ClientErrorCode.CLIENT_NOT_FOUND.message);
        }

        @Test
        @DisplayName("Roadmap이 없는 경우")
        public void roadmapNotFound() {
            //given
            Client client = Client.create("name", "email", "password");
            client.setId(1L);
            given(clientRepository.findById(client.getId())).willReturn(Optional.of(client));

            given(roadmapRepository.findById(any())).willReturn(Optional.empty());

            //when
            Executable executable = () -> roadmapService.likeRoadmap(1L, true, 1L);

            //then
            assertThrows(RestApiException.class, executable, CommonErrorCode.RESOURCE_NOT_FOUND.message);
        }
    }
}