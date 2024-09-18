package com.pandora.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Autowired
    private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // We don't need CSRF for this example
        http.headers()
                .contentTypeOptions()
                .and()
                .xssProtection()
                .and()
                .cacheControl()
                .and()
                .httpStrictTransportSecurity()
                .and()
                .frameOptions()
                .and()
                .addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy","script-src 'self'"))
                .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Resource-Policy","same-site"))
                .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Opener-Policy","same-origin"))
                .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Embedder-Policy","require-corp"))
                .and()
                .csrf()
                .ignoringRequestMatchers("/api/**")
                .and()
                .authorizeHttpRequests().requestMatchers("/admin/dashboard","/admin/security","/admin/security/**").authenticated().and()
                .authorizeHttpRequests().requestMatchers("/api/**","/css/**","/scss/**","/images/**","/js/**","/libs/**","/reset","/reset/**").permitAll().and()
                .authorizeHttpRequests().requestMatchers("/admin/admins/**").hasRole("OWNER").and()
                .authorizeHttpRequests().requestMatchers("/admin/**").hasAnyRole("ADMIN","OWNER").and()
                .authorizeHttpRequests().requestMatchers("/").permitAll().and()
                .formLogin()
                //.authenticationDetailsSource(authenticationDetailsSource)
                .loginPage("/login")
                .loginProcessingUrl("login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successForwardUrl("/admin/dashboard")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout");

        return http.build();
    }
}
