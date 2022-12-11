package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record RoadmapSearch(
        @Min(1) Long client,
        @Size(min = 1, max = 50) String title,   // null: 전체, exist: contains(name)
        Boolean official,   // null: 전체, true: official, false: custom
        RoadmapOrder order,        // null, likes: 좋아요 순, latest: 최신순
        @NotNull Integer page
) { }
