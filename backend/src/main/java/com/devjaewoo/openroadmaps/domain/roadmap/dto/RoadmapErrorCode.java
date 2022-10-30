package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoadmapErrorCode implements ErrorCode {
    INVALID_PARENT(HttpStatus.BAD_REQUEST, "존재하지 않는 부모 노드를 참조하고 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
