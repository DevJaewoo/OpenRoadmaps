package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;

public enum ConnectionType {
    L2L, L2R,
    R2L, R2R,
    T2T, T2B,
    B2T, B2B;

    public static class ConnectionTypeConverter implements Converter<String, ConnectionType> {

        @Override
        public ConnectionType convert(@NonNull String source) {
            return Arrays.stream(values())
                    .filter(c -> c.name().equals(source.toUpperCase()))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 연결 타입입니다."));
        }
    }
}
