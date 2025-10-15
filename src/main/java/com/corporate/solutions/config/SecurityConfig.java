package com.corporate.solutions.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // You’re using stateless HTTP Basic; CSRF isn’t useful here
                .csrf(csrf -> csrf.disable())

                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // ---- Static & landing pages ----
                        .requestMatchers(
                                "/", "/index.html", "/products.html", "/favicon.ico",
                                "/css/**", "/js/**", "/images/**", "/webjars/**"
                        ).permitAll()

                        // ---- CORS preflight ----
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ---- Registration (public POST only) ----
                        .requestMatchers(HttpMethod.POST, "/api/register").permitAll()

                        // ---- Products: read for USER or ADMIN ----
                        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("USER", "ADMIN")

                        // ---- Products: write only for ADMIN ----
                        .requestMatchers("/api/products/**").hasRole("ADMIN")

                        // ---- Audit endpoints: ADMIN only ----
                        .requestMatchers("/api/audit/**").hasRole("ADMIN")

                        // ---- Everything else authenticated ----
                        .anyRequest().authenticated()
                )

                // HTTP Basic is fine for the assignment scope
                .httpBasic(Customizer.withDefaults())

                // No server-side sessions (stateless API)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // If you ever use H2 console locally, uncomment:
        // http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // Delegating encoder supports {bcrypt} etc.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager mgr = new JdbcUserDetailsManager(dataSource);

        // users(username, password, enabled)
        mgr.setUsersByUsernameQuery("""
            SELECT username, password, enabled
            FROM users
            WHERE username = ?
        """);

        // authorities via join users -> users_roles -> roles
        mgr.setAuthoritiesByUsernameQuery("""
            SELECT u.username, r.name AS authority
            FROM users u
            JOIN users_roles ur ON ur.user_id = u.id
            JOIN roles r ON r.id = ur.role_id
            WHERE u.username = ?
        """);

        return mgr;
    }
}