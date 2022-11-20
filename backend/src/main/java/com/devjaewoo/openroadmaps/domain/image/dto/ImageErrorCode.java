package com.devjaewoo.openroadmaps.domain.image.dto;

import com.devjaewoo.openroadmaps.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ImageErrorCode implements ErrorCode {
    BAD_EXTENSION(HttpStatus.BAD_REQUEST, "지원되지 않는 확장자입니다."),
    CREATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 생성 중 오류가 발생했습니다."),
    READ_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 읽기 중 오류가 발생했습니다."),
    ;

    public final HttpStatus httpStatus;
    public final String message;
}
