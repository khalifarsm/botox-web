package com.pandora.api.service;

import com.pandora.api.entity.User;
import com.pandora.api.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.pandora.api.entity.User.ROLE_OWNER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService implements UserDetailsService {

    @Value("${owner}")
    private String owner;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private Map<Long, String> adminEmails = new HashMap<>();

    @PostConstruct
    public void setup(){
        log.info("seed admin execution started");
        if (userRepository.count() == 0) {
            String email = owner.split(":")[0];
            String password = owner.split(":")[1];
            log.info("create default admin");
            createAdmin(email,
                    password,ROLE_OWNER);
        }
    }

    public String getEmail(Long id) {
        if (id == null) {
            return null;
        }
        String email = adminEmails.get(id);
        if (email != null) {
            return email;
        }
        User auth = userRepository.findById(id)
                .orElse(null);
        if (auth == null) {
            return null;
        } else {
            email = auth.getEmail();
        }
        adminEmails.put(id, email);
        return email;
    }

    public void createAdmin(String email, String password, String role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreated(new Date());
        user.setRole(role);
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            User user = new User();
            user.setRole("Anounymous");
            user.setEmail("Anounymous");
            return user;
        }
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findFirstByEmail(currentPrincipalName);
        if (user == null) {
            user = new User();
            user.setRole("Anounymous");
            user.setEmail("Anounymous");
            return user;
        }
        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("login {}", username);
        User user = userRepository.findFirstByEmail(username);
        org.springframework.security.core.userdetails.User.UserBuilder builder = null;
        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.roles(user.getRole());
            builder.password(user.getPassword());

        } else {
            log.info("authentication failed");
            throw new UsernameNotFoundException("user not found");
        }
        return builder.build();
    }
}
