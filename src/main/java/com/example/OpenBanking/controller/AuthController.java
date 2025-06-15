package com.example.OpenBanking.controller;

import com.example.OpenBanking.dto.AuthResponseDTO;
import com.example.OpenBanking.dto.LoginRequestDTO;
import com.example.OpenBanking.model.User;
import com.example.OpenBanking.config.JwtProvider;
import com.example.OpenBanking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> createUser(@RequestBody User user) throws Exception {
        AuthResponseDTO response = userService.registerUser(user, passwordEncoder);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDTO> signIn(@RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponseDTO(token, "Login success", true));
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.userService.loadUserByUsername(email);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

