package com.devjaewoo.openroadmaps.global.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        long totalElements,
        int totalPages
) {
    public PageResponseDto(Page<T> page) {
        this(page.getContent(), page.getTotalElements(), page.getTotalPages());
    }
}
