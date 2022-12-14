package com.devjaewoo.openroadmaps.domain.client.dto;

import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Builder
public record ClientDto(
        Long id,
        String name,
        String email,
        String picture,
        int reputation,
        boolean isEnabled,
        Role role) {

    public static ClientDto from(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .picture(client.getPicture())
                .reputation(client.getReputation())
                .isEnabled(client.isEnabled())
                .role(client.getRole())
                .build();
    }

    @Builder
    public record Response(
            Long id,
            String name,
            String email,
            String picture,
            int reputation) {

        public static Response from(ClientDto client) {
            return Response.builder()
                    .id(client.id)
                    .name(client.name)
                    .email(client.email)
                    .picture(client.picture)
                    .reputation(client.reputation)
                    .build();
        }

        public Response(ClientDto client) {
            this(client.id, client.name, client.email, client.picture, client.reputation);
        }
    }

    public record Register(
            @NotEmpty @Email
            String email,

            @NotEmpty
            @Pattern(
                    regexp = "^[0-9a-z가-힣]{1,10}$",
                    message = "숫자, 영문 소문자, 한글로 이루어진 10자리 이내의 이름이어야 합니다."
            )
            String name,

            @NotEmpty
            @Pattern(
                    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,14}$",
                    message = "하나 이상의 대문자, 소문자, 숫자, 특수문자를 포함한 8~14 자리의 비밀번호이어야 합니다."
            )
            String password) {
    }

    public record LoginRequest(
            @NotEmpty @Email
            String email,

            @NotEmpty
            @Pattern(
                    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,14}$",
                    message = "하나 이상의 대문자, 소문자, 숫자, 특수문자를 포함한 8~14 자리의 비밀번호이어야 합니다."
            )
            String password) {
    }
}
