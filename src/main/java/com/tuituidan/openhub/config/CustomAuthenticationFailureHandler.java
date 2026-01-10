package com.tuituidan.openhub.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * CustomAuthenticationFailureHandler.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026-01-09
 */
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        if (StringUtils.isNotBlank(request.getQueryString())) {
            setDefaultFailureUrl("/login?error&" + request.getQueryString());
        } else {
            super.setDefaultFailureUrl("/login?error");
        }
        super.onAuthenticationFailure(request, response, exception);
    }

}
