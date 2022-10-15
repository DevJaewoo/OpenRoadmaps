package com.devjaewoo.openroadmaps.domain.client;

import com.devjaewoo.openroadmaps.global.exception.CommonErrorCode;
import com.devjaewoo.openroadmaps.global.exception.ErrorResponse;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class ClientControllerTest {

    @LocalServerPort
    private int port;

    @Autowired EntityManager em;

    @DisplayName("회원가입 테스트")
    @Nested
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
            String email = "conflict@email.com";
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
}