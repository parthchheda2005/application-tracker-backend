package com.applicationtracker.applicationtrackerbackend.service;

import com.applicationtracker.applicationtrackerbackend.dto.LoginUserDto;
import com.applicationtracker.applicationtrackerbackend.dto.RegisterUserDto;
import com.applicationtracker.applicationtrackerbackend.model.User;
import com.applicationtracker.applicationtrackerbackend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    public User signup(RegisterUserDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto dto) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getEmail(),
                dto.getPassword()
        ));

        return userRepository.findByEmail(dto.getEmail())
                .orElseThrow();
    }
}
