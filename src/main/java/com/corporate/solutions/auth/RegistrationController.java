package com.corporate.solutions.auth;

import com.corporate.solutions.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserDetailsManager users;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest req) {
        if (users.userExists(req.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        String encoded = encoder.encode(req.getPassword());
        UserDetails u = User.withUsername(req.getUsername())
                .password(encoded) // encoded!
                .roles("USER")
                .build();
        users.createUser(u);
    }
}
