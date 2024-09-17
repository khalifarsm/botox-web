package com.pandora.api.security;

import com.pandora.api.entity.User;
import com.pandora.api.repository.UserRepository;
import com.pandora.api.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminService adminService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        User user;
        try {
            user = adminService.getAuthenticatedUser();
            log.info("user authenticated");
            httpServletResponse.sendRedirect("/admin/dashboard");
        } catch (Exception e) {
            httpServletResponse.sendRedirect("/login");
        }

    }

}