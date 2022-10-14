package com.devjaewoo.openroadmaps.global.handler;

import com.devjaewoo.openroadmaps.global.exception.CommonErrorCode;
import com.devjaewoo.openroadmaps.global.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ForbiddenHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        CommonErrorCode errorCode = CommonErrorCode.FORBIDDEN;
        ErrorResponse errorResponse = new ErrorResponse(errorCode);
        String json = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(errorCode.httpStatus.value());
        response.getWriter().write(json);
        response.flushBuffer();
    }
}
