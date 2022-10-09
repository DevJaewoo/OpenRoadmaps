package com.devjaewoo.openroadmaps.domain.client;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    CLIENT("ROLE_CLIENT");

    public final String key;
}
