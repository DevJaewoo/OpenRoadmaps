package com.devjaewoo.openroadmaps.domain.blog.dto;

import javax.validation.constraints.NotNull;

public record PostSearch(
        String title,           // 포스트 이름
        String clientName,      // 작성자 이름
        Long categoryId,        // 카테고리 ID
        Long roadmapItemId,     // 로드맵 항목 ID
        PostOrder order,
        @NotNull Integer page) {  }
