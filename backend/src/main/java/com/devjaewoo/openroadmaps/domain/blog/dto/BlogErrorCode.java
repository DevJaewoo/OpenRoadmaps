package com.devjaewoo.openroadmaps.domain.blog.dto;

import com.devjaewoo.openroadmaps.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BlogErrorCode implements ErrorCode {
    DUPLICATE_CATEGORY(HttpStatus.CONFLICT, "중복된 카테고리명이 존재합니다."),
    ;

    public final HttpStatus httpStatus;
    public final String message;
}
