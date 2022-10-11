package com.devjaewoo.openroadmaps.global.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUri = determineTargetUrl(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String redirectUrl = request.getParameter("redirect");
        return "http://localhost:3000" + ((redirectUrl != null) ? redirectUrl : "");
    }
}
