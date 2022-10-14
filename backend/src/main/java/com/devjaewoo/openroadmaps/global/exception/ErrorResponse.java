package com.devjaewoo.openroadmaps.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.FieldError;

import java.util.List;

public record ErrorResponse(String code, String message, @JsonInclude(JsonInclude.Include.NON_EMPTY) List<ValidationError> errors) {
    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode.name(), errorCode.getMessage(), null);
    }

    public record ValidationError(String field, String message) {
        public ValidationError(FieldError fieldError) {
            this(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
