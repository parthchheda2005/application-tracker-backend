package com.applicationtracker.applicationtrackerbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${spring.security.jwt-secret}")
    private String jwtSecret;

    @Value("${spring.security.expiration-time}")
    private Long expirationTime;

}
