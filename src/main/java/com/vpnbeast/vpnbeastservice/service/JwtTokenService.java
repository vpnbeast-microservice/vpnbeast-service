package com.vpnbeast.vpnbeastservice.service;

import com.vpnbeast.vpnbeastservice.model.response.JwtResponse;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Function;

public interface JwtTokenService {

    String getUsernameFromToken(String token);
    LocalDateTime getExpiresAt(String token);
    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);
    Boolean validateToken(String token, UserDetails userDetails);
    JwtResponse generateTokenResponse(UserDetails userDetails);
    ArrayList getRolesFromToken(String token);

}