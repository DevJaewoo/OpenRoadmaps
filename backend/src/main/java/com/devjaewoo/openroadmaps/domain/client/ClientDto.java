package com.devjaewoo.openroadmaps.domain.client;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public record ClientDto(
        Long id,
        String name,
        String email,
        String picture,
        int reputation,
        boolean isEnabled) {

    public ClientDto(Client client) {
        this(client.getId(), client.getName(), client.getEmail(), client.getPicture(), client.getReputation(), client.isEnabled());
    }

    public record Response(
            String name,
            String email,
            String picture,
            int reputation) {

        public Response(ClientDto client) {
            this(client.name, client.email, client.picture, client.reputation);
        }
    }

    public record Register(
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
