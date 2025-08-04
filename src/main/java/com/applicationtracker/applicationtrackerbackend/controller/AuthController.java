package com.applicationtracker.applicationtrackerbackend.controller;

import com.applicationtracker.applicationtrackerbackend.dto.LoginResponseDto;
import com.applicationtracker.applicationtrackerbackend.dto.LoginUserDto;
import com.applicationtracker.applicationtrackerbackend.dto.RegisterUserDto;
import com.applicationtracker.applicationtrackerbackend.model.User;
import com.applicationtracker.applicationtrackerbackend.service.AuthService;
import com.applicationtracker.applicationtrackerbackend.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterUserDto req) {
        try {
            User user = authService.signup(req);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred during signup.");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto req) {
        try {
            User user = authService.authenticate(req);
            String token = jwtService.generateToken(user);

            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setToken(token);
            loginResponseDto.setSuccess(true);
            loginResponseDto.setMessage("Successfully logged in.");
            loginResponseDto.setExpiresIn(jwtService.getExpirationTime());

            return ResponseEntity.ok(loginResponseDto);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            LoginResponseDto response = new LoginResponseDto();
            response.setSuccess(false);
            response.setMessage("Invalid email or password.");
            return ResponseEntity.status(401).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred during login.");
        }
    }
}

