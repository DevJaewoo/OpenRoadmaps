package com.devjaewoo.openroadmaps.domain.client;

import com.devjaewoo.openroadmaps.global.exception.CommonErrorCode;
import com.devjaewoo.openroadmaps.global.exception.ErrorResponse;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientControllerTest {

    @LocalServerPort
    private int port;

    @Autowired JdbcTemplate jdbcTemplate;

    @AfterEach
    void clear() {
        jdbcTemplate.execute("TRUNCATE TABLE client");
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class Register {

        @Test
        @DisplayName("성공")
        public void success() {
            String email = "test@email.com";
            ClientDto.Register register = new ClientDto.Register(email, "!Asd1234");
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
                    ClientDto.Register register = new ClientDto.Register(email, "!Asd1234");
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
                ClientDto.Register register = new ClientDto.Register("test@email.com", password);
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
            ClientDto.Register register = new ClientDto.Register(email, "!Asd1234");

            given()
                    .port(port)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(register)
            .when()
                    .post("/api/v1/client/register")
            .then()
                    .statusCode(HttpStatus.SC_OK);

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
                            .statusCode(HttpStatus.SC_CONFLICT)
                            .extract();

            ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);
            assertThat(errorResponse.code()).isEqualTo(ClientErrorCode.DUPLICATE_EMAIL.name());
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class Login {

        String registerEmail = "test@email.com";
        String registerPassword = "!Asd1234";

        @BeforeEach
        void register() {
            ClientDto.Register register = new ClientDto.Register(registerEmail, registerPassword);

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
            ClientDto.Register register = new ClientDto.Register(registerEmail, registerPassword);

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
            assertThat(client.name()).isEqualTo("User#1");
            assertThat(client.email()).isEqualTo(registerEmail.toLowerCase());
        }

        @Test
        @DisplayName("이메일 형식 오류")
        public void emailPatternError() {
            List<String> emailList = Arrays.asList(null, "", "@email.com", "test@.com", "test@email.");

            emailList.forEach((email) -> {
                ClientDto.Register register = new ClientDto.Register(email, "!Asd1234");
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
                ClientDto.Register register = new ClientDto.Register("test@email.com", password);
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
            ClientDto.Register register = new ClientDto.Register("incorrect@email.com", registerPassword);

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
            ClientDto.Register register = new ClientDto.Register(registerEmail, "P@ssw0rd");

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
        @DisplayName("비밀번호 불일치")
        public void disabledClient() {
            //given
            jdbcTemplate.execute("UPDATE client set is_enabled='f' WHERE client_id=1");
            ClientDto.Register register = new ClientDto.Register(registerEmail, registerPassword);

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
}