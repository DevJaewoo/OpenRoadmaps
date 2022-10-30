package com.devjaewoo.openroadmaps.domain.roadmap.dto;

import com.devjaewoo.openroadmaps.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoadmapErrorCode implements ErrorCode {
    INVALID_PARENT(HttpStatus.BAD_REQUEST, "존재하지 않는 부모 노드를 참조하고 있습니다."),
    INVALID_CONNECTION(HttpStatus.BAD_REQUEST, "부모 노드와 연결되어있어야 합니다.");

    public final HttpStatus httpStatus;
    public final String message;
}
