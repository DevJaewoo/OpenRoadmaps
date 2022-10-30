package com.devjaewoo.openroadmaps.domain.client.dto;

import com.devjaewoo.openroadmaps.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClientErrorCode implements ErrorCode {
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일이 존재합니다."),
    UNSUPPORTED_REGISTRATION(HttpStatus.BAD_REQUEST, "지원하지 않는 Registration입니다."),
    INCORRECT_EMAIL(HttpStatus.UNAUTHORIZED, "존재하지 않는 사용자입니다."),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    DISABLED_CLIENT(HttpStatus.UNAUTHORIZED, "비활성화된 계정입니다."),
    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    ;

    public final HttpStatus httpStatus;
    public final String message;
}
