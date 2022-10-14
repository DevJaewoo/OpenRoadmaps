package com.devjaewoo.openroadmaps.domain.client;

import com.devjaewoo.openroadmaps.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClientErrorCode implements ErrorCode {
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일이 존재합니다."),
    ;

    public final HttpStatus httpStatus;
    public final String message;
}
