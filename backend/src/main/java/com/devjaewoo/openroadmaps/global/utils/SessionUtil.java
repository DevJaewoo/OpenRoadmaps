package com.devjaewoo.openroadmaps.global.utils;


import com.devjaewoo.openroadmaps.domain.client.dto.SessionClient;
import com.devjaewoo.openroadmaps.global.config.SessionConfig;
import com.devjaewoo.openroadmaps.global.exception.CommonErrorCode;
import com.devjaewoo.openroadmaps.global.exception.RestApiException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionUtil {

    public static Optional<SessionClient> getOptionalCurrentClient(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = requestAttributes.getRequest().getSession(false);
        if(session != null) {
            return Optional.ofNullable((SessionClient) session.getAttribute(SessionConfig.CLIENT_INFO));
        }
        else {
            return Optional.empty();
        }
    }

    public static SessionClient getCurrentClient() {
        return getOptionalCurrentClient().orElseThrow(() -> new RestApiException(CommonErrorCode.UNAUTHORIZED));
    }
}
