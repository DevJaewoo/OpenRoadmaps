package com.devjaewoo.openroadmaps.global.domain;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;

public enum Accessibility {
    PRIVATE,
    PROTECTED,
    PUBLIC;

    public static class AccessibilityConverter implements Converter<String, Accessibility> {
        @Override
        public Accessibility convert(@NonNull String source) {
            return Arrays.stream(values())
                    .filter((o) -> o.name().equals(source.toUpperCase()))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 접근 조건입니다."));
        }
    }
}
