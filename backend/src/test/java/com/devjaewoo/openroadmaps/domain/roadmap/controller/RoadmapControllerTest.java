package com.devjaewoo.openroadmaps.domain.roadmap.controller;

import com.devjaewoo.openroadmaps.AcceptanceTest;
import com.devjaewoo.openroadmaps.domain.client.dto.ClientDto;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.*;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.domain.roadmap.repository.RoadmapRepository;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import com.devjaewoo.openroadmaps.global.exception.CommonErrorCode;
import com.devjaewoo.openroadmaps.global.exception.ErrorResponse;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

@AcceptanceTest
class RoadmapControllerTest {

    @LocalServerPort
    private int port;

    @Autowired RoadmapRepository roadmapRepository;

    public CookieFilter register(boolean success) {
        return register(success, "email@example.com");
    }

    public CookieFilter register(boolean success, String email) {

        CookieFilter cookieFilter = new CookieFilter();
        if(!success) return cookieFilter;

        ClientDto.Register register = new ClientDto.Register(email, "!@QW12qw");

        given()
                .log().all()
                .port(port)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .filter(cookieFilter)
                .body(register)
                .when()
                .post("/api/v1/client/register")
                .then()
                .statusCode(HttpStatus.SC_OK);

        return cookieFilter;
    }

    @Nested
    @DisplayName("생성")
    class Create {

        @Test
        @DisplayName("성공")
        public void success() {
            // given
            RoadmapItemDto.CreateRequest item1 = new RoadmapItemDto.CreateRequest(1L, "item1", "content1", 0, 0, Recommend.RECOMMEND, null, List.of(), null);
            RoadmapItemDto.CreateRequest item2 = new RoadmapItemDto.CreateRequest(2L, "item2", "content2", 1, 1, Recommend.NOT_RECOMMEND, ConnectionType.b2b, List.of(), 1L);
            RoadmapItemDto.CreateRequest item3 = new RoadmapItemDto.CreateRequest(3L, "item3", "content3", 2, 2, Recommend.ALTERNATIVE, ConnectionType.b2b, List.of(), 1L);
            RoadmapDto.CreateRequest request = new RoadmapDto.CreateRequest("title", "https://image.com", Accessibility.PUBLIC, List.of(item1, item2, item3));
            CookieFilter cookieFilter = register(true);

            // when
            ExtractableResponse<Response> response = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter)
                    .body(request)
                    .when()
                    .post("/api/v1/roadmaps")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract();

            // then
            RoadmapDto.CreateResponse createResponse = response.body().jsonPath().getObject(".", RoadmapDto.CreateResponse.class);
            assertThat(createResponse.roadmapId()).isGreaterThan(0);
        }

        @Test
        @DisplayName("로그인 되지 않은 상태로 시도")
        public void withoutLogin() {
            RoadmapDto.CreateRequest request = new RoadmapDto.CreateRequest("title", "https://image.com", Accessibility.PUBLIC, List.of());
            CookieFilter cookieFilter = register(false);

            // when
            ExtractableResponse<Response> response = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter)
                    .body(request)
                    .when()
                    .post("/api/v1/roadmaps")
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED)
                    .extract();

            // then
            ErrorResponse errorResponse = response.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(CommonErrorCode.UNAUTHORIZED.name());
        }

        @Test
        @DisplayName("잘못된 부모 등록")
        public void invalidParent() {
            // given
            RoadmapItemDto.CreateRequest item1 = new RoadmapItemDto.CreateRequest(1L, "item1", "content1", 0, 0, Recommend.RECOMMEND, null, List.of(), null);
            RoadmapItemDto.CreateRequest item2 = new RoadmapItemDto.CreateRequest(2L, "item2", "content2", 1, 1, Recommend.NOT_RECOMMEND, ConnectionType.b2b, List.of(), 2L);
            RoadmapItemDto.CreateRequest item3 = new RoadmapItemDto.CreateRequest(2L, "item2", "content2", 1, 1, Recommend.NOT_RECOMMEND, ConnectionType.b2b, List.of(), 3L);
            RoadmapDto.CreateRequest request1 = new RoadmapDto.CreateRequest("title", "https://image.com", Accessibility.PUBLIC, List.of(item1, item2));
            RoadmapDto.CreateRequest request2 = new RoadmapDto.CreateRequest("title", "https://image.com", Accessibility.PUBLIC, List.of(item1, item3));
            CookieFilter cookieFilter = register(true);

            // when
            ExtractableResponse<Response> response1 = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter)
                    .body(request2)
                    .when()
                    .post("/api/v1/roadmaps")
                    .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .extract();

            ExtractableResponse<Response> response2 = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter)
                    .body(request1)
                    .when()
                    .post("/api/v1/roadmaps")
                    .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .extract();

            // then
            ErrorResponse errorResponse1 = response1.body().jsonPath().getObject(".", ErrorResponse.class);
            ErrorResponse errorResponse2 = response2.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse1.code()).isEqualTo(RoadmapErrorCode.INVALID_PARENT.name());
            assertThat(errorResponse2.code()).isEqualTo(RoadmapErrorCode.INVALID_PARENT.name());
        }

        @Test
        @DisplayName("잘못된 연결선 등록")
        public void invalidConnection() {
            RoadmapItemDto.CreateRequest item1 = new RoadmapItemDto.CreateRequest(1L, "item1", "content1", 0, 0, Recommend.RECOMMEND, null, List.of(), null);
            RoadmapItemDto.CreateRequest item2 = new RoadmapItemDto.CreateRequest(2L, "item2", "content2", 1, 1, Recommend.NOT_RECOMMEND, null, List.of(), 1L);
            RoadmapDto.CreateRequest request = new RoadmapDto.CreateRequest("title", "https://image.com", Accessibility.PUBLIC, List.of(item1, item2));
            CookieFilter cookieFilter = register(true);

            // when
            ExtractableResponse<Response> response = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter)
                    .body(request)
                    .when()
                    .post("/api/v1/roadmaps")
                    .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .extract();

            // then
            ErrorResponse errorResponse1 = response.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse1.code()).isEqualTo(RoadmapErrorCode.INVALID_CONNECTION.name());
        }
    }

    @Nested
    @DisplayName("조회")
    class Find {

        @Test
        @DisplayName("성공")
        public void success() {
            // given
            CookieFilter cookieFilter = register(true);
            RoadmapDto.CreateRequest createRequest = new RoadmapDto.CreateRequest("title", null, Accessibility.PRIVATE, List.of());

            ExtractableResponse<Response> createResponse = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter)
                    .body(createRequest)
                    .when()
                    .post("/api/v1/roadmaps")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract();

            Long roadmapId = createResponse.body().jsonPath().getObject(".", RoadmapDto.CreateResponse.class).roadmapId();

            // when
            ExtractableResponse<Response> response = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter)
                    .when()
                    .get("/api/v1/roadmaps/" + roadmapId)
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract();

            // then
            RoadmapDto.Response roadmapResponse = response.body().jsonPath().getObject(".", RoadmapDto.Response.class);
            assertThat(roadmapResponse.id()).isEqualTo(roadmapId);
            assertThat(roadmapResponse.title()).isEqualTo(createRequest.title());
            assertThat(roadmapResponse.image()).isEqualTo(createRequest.image());
            assertThat(roadmapResponse.accessibility()).isEqualTo(createRequest.accessibility().name());
        }

        @Test
        @DisplayName("존재하지 않는 로드맵 조회")
        public void notFound() {
            // given
            CookieFilter cookieFilter = register(false);

            // when
            ExtractableResponse<Response> response = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter)
                    .when()
                    .get("/api/v1/roadmaps/999")
                    .then()
                    .statusCode(HttpStatus.SC_NOT_FOUND)
                    .extract();

            // then
            ErrorResponse errorResponse = response.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(CommonErrorCode.RESOURCE_NOT_FOUND.name());
        }

        @Test
        @DisplayName("권한이 없는 로드맵 조회")
        public void notAccessible() {
            CookieFilter cookieFilter1 = register(true);
            RoadmapDto.CreateRequest createRequest = new RoadmapDto.CreateRequest("title", null, Accessibility.PRIVATE, List.of());

            CookieFilter cookieFilter2 = register(true, "forbidden@example.com");

            ExtractableResponse<Response> createResponse = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter1)
                    .body(createRequest)
                    .when()
                    .post("/api/v1/roadmaps")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract();

            Long roadmapId = createResponse.body().jsonPath().getObject(".", RoadmapDto.CreateResponse.class).roadmapId();

            // when
            ExtractableResponse<Response> response1 = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/api/v1/roadmaps/" + roadmapId)
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED)
                    .extract();

            ExtractableResponse<Response> response2 = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter2)
                    .when()
                    .get("/api/v1/roadmaps/" + roadmapId)
                    .then()
                    .statusCode(HttpStatus.SC_FORBIDDEN)
                    .extract();

            // then
            ErrorResponse errorResponse1 = response1.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse1.code()).isEqualTo(CommonErrorCode.UNAUTHORIZED.name());

            ErrorResponse errorResponse2 = response2.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse2.code()).isEqualTo(CommonErrorCode.FORBIDDEN.name());
        }
    }

    @Nested
    @DisplayName("로드맵 항목 완료")
    class ItemClear {

        @Test
        @DisplayName("성공")
        public void success() {
            //given
            CookieFilter cookieFilter = register(true);
            Roadmap roadmap = Roadmap.create("title1", "image", Accessibility.PUBLIC, null);
            RoadmapItem roadmapItem = RoadmapItem.create("item", "content", 1, 1, null, null, null, roadmap);

            roadmapRepository.save(roadmap);
            roadmapRepository.flush();

            boolean cleared = true;
            RoadmapItemClearDto.ClearRequest clearRequest = new RoadmapItemClearDto.ClearRequest(cleared);

            //when
            ExtractableResponse<Response> response = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter)
                    .body(clearRequest)
                    .when()
                    .put("/api/v1/roadmaps/" + roadmap.getId() + "/items/" + roadmapItem.getId() + "/clear")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract();

            //then
            RoadmapItemClearDto.ClearResponse clearResponse = response.body().jsonPath().getObject(".", RoadmapItemClearDto.ClearResponse.class);
            assertThat(clearResponse.roadmapItemId()).isEqualTo(roadmapItem.getId());
            assertThat(clearResponse.isCleared()).isEqualTo(cleared);
        }

        @Test
        @DisplayName("로그인 되지 않은 상태로 시도")
        public void withoutLogin() {
            RoadmapItemClearDto.ClearRequest clearRequest = new RoadmapItemClearDto.ClearRequest(true);

            // when
            ExtractableResponse<Response> response = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(clearRequest)
                    .when()
                    .patch("/api/v1/roadmaps/1/items/1")
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED)
                    .extract();

            // then
            ErrorResponse errorResponse = response.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(CommonErrorCode.UNAUTHORIZED.name());
        }
    }

    @Nested
    @DisplayName("로드맵 좋아요")
    class Like {

        @Test
        @DisplayName("성공")
        public void success() {
            //given
            CookieFilter cookieFilter = register(true);
            Roadmap roadmap = Roadmap.create("title1", "image", Accessibility.PUBLIC, null);
            roadmapRepository.save(roadmap);

            boolean like = true;
            RoadmapLikeDto.LikeRequest likeRequest = new RoadmapLikeDto.LikeRequest(like);

            //when
            ExtractableResponse<Response> response = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter)
                    .body(likeRequest)
                    .when()
                    .put("/api/v1/roadmaps/" + roadmap.getId() + "/like")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract();

            //then
            RoadmapLikeDto.LikeResponse likeResponse = response.body().jsonPath().getObject(".", RoadmapLikeDto.LikeResponse.class);
            assertThat(likeResponse.roadmapId()).isEqualTo(roadmap.getId());
            assertThat(likeResponse.like()).isEqualTo(like);
        }

        @Test
        @DisplayName("로그인 되지 않은 상태로 시도")
        public void withoutLogin() {
            RoadmapLikeDto.LikeRequest likeRequest = new RoadmapLikeDto.LikeRequest(true);

            // when
            ExtractableResponse<Response> response = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(likeRequest)
                    .when()
                    .patch("/api/v1/roadmaps/1/like")
                    .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED)
                    .extract();

            // then
            ErrorResponse errorResponse = response.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(CommonErrorCode.UNAUTHORIZED.name());
        }
    }
}