package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;

public enum RoadmapOrder {
    LIKES,
    LATEST;

    public static class RoadmapOrderConverter implements Converter<String, RoadmapOrder> {

        @Override
        public RoadmapOrder convert(@NonNull String source) {
            return Arrays.stream(RoadmapOrder.values())
                    .filter((o) -> o.name().equals(source.toUpperCase()))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 정렬 조건입니다."));
        }
    }
}