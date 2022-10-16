package com.devjaewoo.openroadmaps.domain.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SessionClient implements Serializable {
    private Long id;
    private String name;

    public SessionClient(ClientDto clientDto) {
        this.id = clientDto.id();
        this.name = clientDto.name();
    }
}
