package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;

public record RoadmapSearch(
        @Min(1) Long client,
        @Size(min = 1, max = 50) String name,   // null: 전체, exist: contains(name)
        Boolean official,   // null: 전체, true: official, false: custom
        Order order,        // null, like: 좋아요 순, latest: 최신순
        @NotNull Integer page
) {

    public enum Order {
        LIKE,
        LATEST;

        public static class OrderConverter implements Converter<String, Order> {

            @Override
            public Order convert(@NonNull String source) {
                return Arrays.stream(Order.values())
                        .filter((o) -> o.name().equals(source.toUpperCase()))
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("잘못된 정렬 조건입니다."));
            }
        }
    }

}
