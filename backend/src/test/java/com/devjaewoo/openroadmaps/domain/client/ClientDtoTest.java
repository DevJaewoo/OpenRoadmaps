package com.devjaewoo.openroadmaps.domain.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClientDtoTest {

    @Test
    @DisplayName("AllArgsContructor 테스트")
    public void testAllArgsConstructor() {
        //given
        long id = 1L;
        String name = "name";
        String email = "email";
        String picture = "picture";
        int reputation = 123;
        boolean isEnabled = true;
        Role role = Role.CLIENT;

        //when
        ClientDto clientDto = new ClientDto(id, name, email, picture, reputation, isEnabled, role);

        //then
        assertThat(clientDto.id()).isEqualTo(id);
        assertThat(clientDto.name()).isEqualTo(name);
        assertThat(clientDto.email()).isEqualTo(email);
        assertThat(clientDto.picture()).isEqualTo(picture);
        assertThat(clientDto.reputation()).isEqualTo(reputation);
        assertThat(clientDto.isEnabled()).isEqualTo(isEnabled);
        assertThat(clientDto.role()).isEqualTo(role);
    }

    @Test
    @DisplayName("ClientContructor 테스트")
    public void testClientConstructor() {
        //given
        Client client = new Client();
        client.setId(1L);
        client.setName("name");
        client.setEmail("email");
        client.setPicture("picture");
        client.setReputation(123);
        client.setEnabled(true);
        client.setRole(Role.CLIENT);

        //when
        ClientDto clientDto = new ClientDto(client);

        //then
        assertThat(clientDto.id()).isEqualTo(client.getId());
        assertThat(clientDto.name()).isEqualTo(client.getName());
        assertThat(clientDto.email()).isEqualTo(client.getEmail());
        assertThat(clientDto.picture()).isEqualTo(client.getPicture());
        assertThat(clientDto.reputation()).isEqualTo(client.getReputation());
        assertThat(clientDto.isEnabled()).isEqualTo(client.isEnabled());
        assertThat(clientDto.role()).isEqualTo(client.getRole());
    }
}