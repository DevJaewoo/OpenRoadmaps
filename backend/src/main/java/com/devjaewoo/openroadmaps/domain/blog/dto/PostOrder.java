package com.devjaewoo.openroadmaps.domain.blog.dto;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;

public enum PostOrder {
    LIKES,
    LATEST;

    public static class PostOrderConverter implements Converter<String, PostOrder> {

        @Override
        public PostOrder convert(@NonNull String source) {
            return Arrays.stream(PostOrder.values())
                    .filter((o) -> o.name().equals(source.toUpperCase()))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 정렬 조건입니다."));
        }
    }
}
