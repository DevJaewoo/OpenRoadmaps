package com.devjaewoo.openroadmaps.domain.client.controller;

import com.devjaewoo.openroadmaps.AcceptanceTest;
import com.devjaewoo.openroadmaps.domain.client.dto.ClientDto;
import com.devjaewoo.openroadmaps.domain.client.dto.ClientErrorCode;
import com.devjaewoo.openroadmaps.global.exception.CommonErrorCode;
import com.devjaewoo.openroadmaps.global.exception.ErrorResponse;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
class ClientControllerTest {

    @LocalServerPort
    private int port;

    @Autowired JdbcTemplate jdbcTemplate;

    @Nested
    @DisplayName("회원가입 테스트")
    class Register {

        @Test
        @DisplayName("성공")
        public void success() {
            String email = "test@email.com";
            ClientDto.Register register = new ClientDto.Register(email, "name", "!Asd1234");
            ExtractableResponse<Response> response =
                    given()
                            .log().all()
                            .port(port)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .body(register)
                    .when()
                            .post("/api/v1/client/register")
                    .then()
                            .log().all()
                            .statusCode(HttpStatus.SC_OK)
                            .extract();

            ClientDto.Response client = response.jsonPath().getObject(".", ClientDto.Response.class);
            assertThat(client.email()).isEqualTo(email);
            assertThat(client.reputation()).isZero();
        }

        @Test
        @DisplayName("이메일 형식 오류")
        public void emailPatternError() {
            List<String> emailList = Arrays.asList(null, "", "@email.com", "test@.com", "test@email.");

            emailList.forEach((email) -> {
                ClientDto.Register register = new ClientDto.Register(email, "name", "!Asd1234");
                ExtractableResponse<Response> response =
                        given()
                                .log().all()
                                .port(port)
                                .accept(ContentType.JSON)
                                .contentType(ContentType.JSON)
                                .body(register)
                                .when()
                                .post("/api/v1/client/register")
                                .then()
                                .log().all()
                                .statusCode(HttpStatus.SC_BAD_REQUEST)
                                .extract();

                ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
                assertThat(errorResponse.code()).isEqualTo(CommonErrorCode.INVALID_PARAMETER.name());
            });
        }

        @Test
        @DisplayName("이름 형식 오류")
        public void namePatternError() {
            List<String> nameList = Arrays.asList(null, "", "A", "abcdefghijklmnop", "!@#$");

            nameList.forEach((name) -> {
                ClientDto.Register register = new ClientDto.Register("test@gmail.com", name, "!Asd1234");
                ExtractableResponse<Response> response =
                        given()
                                .log().all()
                                .port(port)
                                .accept(ContentType.JSON)
                                .contentType(ContentType.JSON)
                                .body(register)
                                .when()
                                .post("/api/v1/client/register")
                                .then()
                                .log().all()
                                .statusCode(HttpStatus.SC_BAD_REQUEST)
                                .extract();

                ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
                assertThat(errorResponse.code()).isEqualTo(CommonErrorCode.INVALID_PARAMETER.name());
            });
        }

        @Test
        @DisplayName("비밀번호 패턴 불일치")
        public void passwordPatternError() {
            List<String> passwordList = Arrays.asList(
                    null,   // null
                    "",     // empty
                    "a",    // length < 8
                    "abcdefghijklmnopqrstuvwxyz",   // length > 14
                    "!ABCDE123",    // Does not contain lowercase
                    "!abcde123",    // Does not contain uppercase
                    "Abcde1234",    // Does not contain special character
                    "!@#Abcdef"     // Does not contain number
            );

            passwordList.forEach((password) -> {
                ClientDto.Register register = new ClientDto.Register("test@email.com", "name", password);
                ExtractableResponse<Response> response =
                        given()
                                .log().all()
                                .port(port)
                                .accept(ContentType.JSON)
                                .contentType(ContentType.JSON)
                                .body(register)
                        .when()
                                .post("/api/v1/client/register")
                        .then()
                                .log().all()
                                .statusCode(HttpStatus.SC_BAD_REQUEST)
                                .extract();

                ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
                assertThat(errorResponse.code()).isEqualTo(CommonErrorCode.INVALID_PARAMETER.name());
            });
        }

        @Test
        @DisplayName("이메일 중복")
        public void emailConflict() {
            String email = "test@email.com";
            ClientDto.Register register1 = new ClientDto.Register(email, "name", "!Asd1234");

            given()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(register1)
                    .when()
                    .post("/api/v1/client/register")
                    .then()
                    .statusCode(HttpStatus.SC_OK);

            ClientDto.Register register2 = new ClientDto.Register(email, "name2", "!Asd1234");
            ExtractableResponse<Response> response =
                    given()
                            .log().all()
                            .port(port)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .body(register2)
                            .when()
                            .post("/api/v1/client/register")
                            .then()
                            .log().all()
                            .statusCode(HttpStatus.SC_CONFLICT)
                            .extract();

            ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(ClientErrorCode.DUPLICATE_EMAIL.name());
        }

        @Test
        @DisplayName("이메일 중복")
        public void nameConflict() {
            String name = "name";
            ClientDto.Register register1 = new ClientDto.Register("test@email.com", name, "!Asd1234");

            given()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(register1)
                    .when()
                    .post("/api/v1/client/register")
                    .then()
                    .statusCode(HttpStatus.SC_OK);

            ClientDto.Register register2 = new ClientDto.Register("test2@email.com", name, "!Asd1234");
            ExtractableResponse<Response> response =
                    given()
                            .log().all()
                            .port(port)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .body(register2)
                            .when()
                            .post("/api/v1/client/register")
                            .then()
                            .log().all()
                            .statusCode(HttpStatus.SC_CONFLICT)
                            .extract();

            ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(ClientErrorCode.DUPLICATE_NAME.name());
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class Login {

        String registerEmail = "test@email.com";
        String registerName = "name";
        String registerPassword = "!Asd1234";

        @BeforeEach
        void register() {
            ClientDto.Register register = new ClientDto.Register(registerEmail, registerName, registerPassword);

            given()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .sessionId("SESSION")
                    .body(register)
            .when()
                    .post("/api/v1/client/register")
             .then()
                    .statusCode(HttpStatus.SC_OK);
        }

        @Test
        @DisplayName("성공")
        public void success() {
            //given
            ClientDto.LoginRequest register = new ClientDto.LoginRequest(registerEmail, registerPassword);

            //when
            ExtractableResponse<Response> response =
                    given()
                        .log().all()
                        .port(port)
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .body(register)
                    .when()
                        .post("/api/v1/client/login")
                    .then()
                        .statusCode(HttpStatus.SC_OK)
                        .extract();

            //then
            ClientDto.Response client = response.body().jsonPath().getObject(".", ClientDto.Response.class);
            assertThat(client.name()).isEqualTo(registerName);
            assertThat(client.email()).isEqualTo(registerEmail.toLowerCase());
        }

        @Test
        @DisplayName("이메일 형식 오류")
        public void emailPatternError() {
            List<String> emailList = Arrays.asList(null, "", "@email.com", "test@.com", "test@email.");

            emailList.forEach((email) -> {
                ClientDto.LoginRequest register = new ClientDto.LoginRequest(email, "!Asd1234");
                ExtractableResponse<Response> response =
                        given()
                                .log().all()
                                .port(port)
                                .accept(ContentType.JSON)
                                .contentType(ContentType.JSON)
                                .body(register)
                        .when()
                                .post("/api/v1/client/login")
                        .then()
                                .log().all()
                                .statusCode(HttpStatus.SC_BAD_REQUEST)
                                .extract();

                ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
                assertThat(errorResponse.code()).isEqualTo(CommonErrorCode.INVALID_PARAMETER.name());
            });
        }

        @Test
        @DisplayName("비밀번호 패턴 불일치")
        public void passwordPatternError() {
            List<String> passwordList = Arrays.asList(
                    null,   // null
                    "",     // empty
                    "a",    // length < 8
                    "abcdefghijklmnopqrstuvwxyz",   // length > 14
                    "!ABCDE123",    // Does not contain lowercase
                    "!abcde123",    // Does not contain uppercase
                    "Abcde1234",    // Does not contain special character
                    "!@#Abcdef"     // Does not contain number
            );

            passwordList.forEach((password) -> {
                ClientDto.LoginRequest register = new ClientDto.LoginRequest("test@email.com", password);
                ExtractableResponse<Response> response =
                        given()
                                .log().all()
                                .port(port)
                                .accept(ContentType.JSON)
                                .contentType(ContentType.JSON)
                                .body(register)
                        .when()
                                .post("/api/v1/client/login")
                        .then()
                                .log().all()
                                .statusCode(HttpStatus.SC_BAD_REQUEST)
                                .extract();

                ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
                assertThat(errorResponse.code()).isEqualTo(CommonErrorCode.INVALID_PARAMETER.name());
            });
        }

        @Test
        @DisplayName("존재하지 않는 사용자")
        public void incorrectEmail() {
            //given
            ClientDto.LoginRequest register = new ClientDto.LoginRequest("incorrect@email.com", registerPassword);

            //when
            ExtractableResponse<Response> response =
                    given()
                            .log().all()
                            .port(port)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .body(register)
                    .when()
                            .post("/api/v1/client/login")
                    .then()
                            .statusCode(HttpStatus.SC_UNAUTHORIZED)
                            .extract();

            //then
            ErrorResponse errorResponse = response.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(ClientErrorCode.INCORRECT_EMAIL.name());
        }

        @Test
        @DisplayName("비밀번호 불일치")
        public void incorrectPassword() {
            //given
            ClientDto.LoginRequest register = new ClientDto.LoginRequest(registerEmail, "P@ssw0rd");

            //when
            ExtractableResponse<Response> response =
                    given()
                            .log().all()
                            .port(port)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .body(register)
                    .when()
                            .post("/api/v1/client/login")
                    .then()
                            .statusCode(HttpStatus.SC_UNAUTHORIZED)
                            .extract();

            //then
            ErrorResponse errorResponse = response.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(ClientErrorCode.INCORRECT_PASSWORD.name());
        }

        @Test
        @DisplayName("비활성화된 사용자")
        public void disabledClient() {
            //given
            jdbcTemplate.execute("UPDATE client set is_enabled='f' WHERE email='" + registerEmail + "'");
            ClientDto.LoginRequest register = new ClientDto.LoginRequest(registerEmail, registerPassword);

            //when
            ExtractableResponse<Response> response =
                    given()
                            .log().all()
                            .port(port)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .body(register)
                    .when()
                            .post("/api/v1/client/login")
                    .then()
                            .statusCode(HttpStatus.SC_UNAUTHORIZED)
                            .extract();

            //then
            ErrorResponse errorResponse = response.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(ClientErrorCode.DISABLED_CLIENT.name());
        }
    }

    @Nested
    @DisplayName("로그아웃")
    class Logout {

        @Test
        @DisplayName("성공")
        public void success() {
            //given
            ClientDto.Register register = new ClientDto.Register("test@email.com", "name", "!Asd1234");
            CookieFilter cookieFilter = new CookieFilter(false);

            given()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(register)
            .when()
                    .post("/api/v1/client/register")
            .then()
                    .statusCode(HttpStatus.SC_OK);

            //when
            given()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(register)
                    .filter(cookieFilter)
            .when()
                    .post("/api/v1/client/login")
            .then()
                    .statusCode(HttpStatus.SC_OK);

            //then
            given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .filter(cookieFilter)
                    .when()
                    .get("/api/v1/client/logout")
                    .then()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
        }

        @Test
        @DisplayName("로그인되지 않은 상태로 로그아웃 시도")
        public void notLoggedIn() {
            //given

            //when
            ExtractableResponse<Response> response = given()
                    .log().all()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
            .when()
                    .get("/api/v1/client/logout")
            .then()
                    .statusCode(HttpStatus.SC_UNAUTHORIZED)
                    .extract();

            //then
            ErrorResponse errorResponse = response.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(CommonErrorCode.UNAUTHORIZED.name());
        }
    }

    @Nested
    @DisplayName("Client 정보")
    class ClientInfo {

        @Test
        @DisplayName("인증되지 않은 상태로 조회")
        public void getCurrentClientWithoutAuthentication() {
            // given

            // when
            ExtractableResponse<Response> response =
                    given()
                            .log().all()
                            .port(port)
                            .contentType(ContentType.JSON)
                            .accept(ContentType.JSON)
                    .when()
                            .get("/api/v1/client")
                    .then()
                            .statusCode(HttpStatus.SC_UNAUTHORIZED)
                            .extract();

            // then
            ErrorResponse errorResponse = response.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(CommonErrorCode.UNAUTHORIZED.name());
        }

        @Test
        @DisplayName("회원가입 후 조회")
        public void getCurrentClientAfterRegister() {
            // given
            String email = "TEST@email.com";
            String name = "name";
            ClientDto.Register register = new ClientDto.Register(email, name, "!Asd1234");
            CookieFilter cookieFilter = new CookieFilter(false);

            // 회원가입 및 세션 쿠키 저장
            given()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(register)
                    .filter(cookieFilter)
            .when()
                    .post("/api/v1/client/register")
            .then()
                    .statusCode(HttpStatus.SC_OK);

            // when
            ExtractableResponse<Response> response =
                    given()
                            .port(port)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .filter(cookieFilter)
                    .when()
                            .get("/api/v1/client")
                    .then()
                            .statusCode(HttpStatus.SC_OK)
                            .extract();

            // then
            ClientDto.Response clientDto = response.body().jsonPath().getObject(".", ClientDto.Response.class);
            assertThat(clientDto.email()).isEqualTo(email.toLowerCase());
            assertThat(clientDto.name()).isEqualTo(name);
        }

        //로그인 후 현재 사용자 조회
        @Test
        @DisplayName("로그인 후 조회")
        public void getCurrentClientAfterLogin() {
            // given
            String email = "TEST@email.com";
            String name = "name";
            ClientDto.Register register = new ClientDto.Register(email, name, "!Asd1234");
            CookieFilter cookieFilter = new CookieFilter(false);

            // 회원가입
            given()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(register)
            .when()
                    .post("/api/v1/client/register")
            .then()
                    .statusCode(HttpStatus.SC_OK);


            // 로그인 및 세션 쿠키 저장
            given()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(register)
                    .filter(cookieFilter)
            .when()
                    .post("/api/v1/client/login")
            .then()
                    .statusCode(HttpStatus.SC_OK);

            // when
            ExtractableResponse<Response> response =
                    given()
                            .port(port)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .filter(cookieFilter)
                    .when()
                            .get("/api/v1/client")
                    .then()
                            .statusCode(HttpStatus.SC_OK)
                            .extract();

            // then
            ClientDto.Response clientDto = response.body().jsonPath().getObject(".", ClientDto.Response.class);
            assertThat(clientDto.email()).isEqualTo(email.toLowerCase());
            assertThat(clientDto.name()).isEqualTo(name);
        }

        @Test
        @DisplayName("ID로 조회")
        public void findClientById() {
            // given
            String email = "TEST@email.com";
            ClientDto.Register register = new ClientDto.Register(email, "name", "!Asd1234");

            // 회원가입
            ExtractableResponse<Response> registerResponse = given()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(register)
                    .when()
                    .post("/api/v1/client/register")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract();

            Long clientId = registerResponse.body().jsonPath().getObject("id", Long.class);

            // when
            ExtractableResponse<Response> response =
                    given()
                            .port(port)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                    .when()
                            .get("/api/v1/client/" + clientId)
                    .then()
                            .statusCode(HttpStatus.SC_OK)
                            .extract();

            // then
            ClientDto.Response clientDto = response.body().jsonPath().getObject(".", ClientDto.Response.class);
            assertThat(clientDto.id()).isEqualTo(clientId);
            assertThat(clientDto.email()).isEqualTo(email.toLowerCase());
        }

        @Test
        @DisplayName("존재하지 않는 사용자 조회")
        public void findNotExistingClient() {
            // given

            // when
            ExtractableResponse<Response> response =
                    given()
                            .port(port)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                    .when()
                            .get("/api/v1/client/999")
                    .then()
                            .statusCode(HttpStatus.SC_NOT_FOUND)
                            .extract();

            // then
            ErrorResponse errorResponse = response.body().jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(ClientErrorCode.CLIENT_NOT_FOUND.name());
        }
    }
}