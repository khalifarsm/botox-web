package com.pandora.api.Controllers;

import com.pandora.api.entity.User;
import com.pandora.api.repository.UserRepository;
import com.pandora.api.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.io.IOException;
import java.util.Map;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AdminService adminService;
    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @PostMapping(value = "/login")
    public String login(Model model, HttpServletRequest request, String username, String password, String code) {
        if (password == null || password.isEmpty()) {
            String email = username;
            if (email != null && !email.isEmpty()) {
                User user = userRepository.findFirstByEmail(email);
                model.addAttribute("email", email);
                if (user == null) {
                    model.addAttribute("mfa", true);
                } else {
                    model.addAttribute("mfa", user.getIsUsing2FA() != null ? user.getIsUsing2FA() : false);
                }
            }
            return "login";
        }
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(username, password);
        User user = userRepository.findFirstByEmail(username);
        if (user.getIsUsing2FA() != null ? user.getIsUsing2FA() : false) {
            Totp totp = new Totp(user.getSecret());
            if (!isValidLong(code) || !totp.verify(code)) {
                return "redirect:/login?error";
            }
        }
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
        return "redirect:/admin/dashboard";
    }

    @GetMapping(value = "")
    public String home() {
        return "home/index";
    }
}
