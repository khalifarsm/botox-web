package com.pandora.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
@EnableScheduling
public class PandoraAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(PandoraAdminApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder argon2PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
