package com.devjaewoo.openroadmaps.global.exception;

public record ErrorResponse(String code, String message) {
    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode.name(), errorCode.getMessage());
    }
}
