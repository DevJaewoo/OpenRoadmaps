package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;

public enum Recommend {
    RECOMMEND,
    ALTERNATIVE,
    NOT_RECOMMEND,
    NONE;

    public static class RecommendConverter implements Converter<String, Recommend> {
        @Override
        public Recommend convert(@NonNull String source) {
            return Arrays.stream(values())
                    .filter(r -> r.name().equals(source.toUpperCase()))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 추천 조건입니다."));
        }
    }
}
