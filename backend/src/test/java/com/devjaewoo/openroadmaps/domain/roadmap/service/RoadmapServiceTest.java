package com.devjaewoo.openroadmaps.domain.roadmap.service;

import com.devjaewoo.openroadmaps.domain.client.dto.ClientErrorCode;
import com.devjaewoo.openroadmaps.domain.client.dto.SessionClient;
import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.domain.client.repository.ClientRepository;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.ConnectionType;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapDto;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapErrorCode;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapItemDto;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.domain.roadmap.repository.RoadmapRepository;
import com.devjaewoo.openroadmaps.global.config.SessionConfig;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RoadmapServiceTest {

    @Mock ClientRepository clientRepository;
    @Mock RoadmapRepository roadmapRepository;

    @InjectMocks
    RoadmapService roadmapService;

    private void setMockSessionAttribute(String name, Object attribute) {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);

        given(httpServletRequest.getSession(false)).willReturn(httpSession);
        given(httpSession.getAttribute(name)).willReturn(attribute);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));
    }

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
            RoadmapDto result = roadmapService.findById(roadmap.getId());

            //then
            assertThat(result.id()).isEqualTo(roadmap.getId());
        }

        @Test
        @DisplayName("존재하지 않는 로드맵 조회")
        public void findNotExistingRoadmap() {
            //given
            given(roadmapRepository.findById(any())).willReturn(Optional.empty());

            //when
            Executable executable = () -> roadmapService.findById(1L);

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

            setMockSessionAttribute(SessionConfig.CLIENT_INFO, new SessionClient(client.getId(), client.getName()));
            given(roadmapRepository.findById(roadmap.getId())).willReturn(Optional.of(roadmap));

            //when
            RoadmapDto result = roadmapService.findById(roadmap.getId());

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
            setMockSessionAttribute(SessionConfig.CLIENT_INFO, null);

            //when
            Executable executable = () -> roadmapService.findById(roadmap.getId());

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
            setMockSessionAttribute(SessionConfig.CLIENT_INFO, new SessionClient(999L, ""));

            //when
            Executable executable = () -> roadmapService.findById(roadmap.getId());

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
            RoadmapItemDto.CreateRequest node2 = new RoadmapItemDto.CreateRequest(2L, "node1", "content", 0, 0, null, ConnectionType.B2T, null, 1L);
            RoadmapItemDto.CreateRequest node3 = new RoadmapItemDto.CreateRequest(3L, "node1", "content", 0, 0, null, ConnectionType.B2T, null, 1L);
            RoadmapItemDto.CreateRequest node4 = new RoadmapItemDto.CreateRequest(4L, "node1", "content", 0, 0, null, ConnectionType.B2T, null, 2L);
            RoadmapItemDto.CreateRequest node5 = new RoadmapItemDto.CreateRequest(5L, "node1", "content", 0, 0, null, ConnectionType.B2T, null, 3L);

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
            RoadmapItemDto.CreateRequest node2 = new RoadmapItemDto.CreateRequest(2L, "node1", "content", 0, 0, null, ConnectionType.B2T, null, 6L);

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
}