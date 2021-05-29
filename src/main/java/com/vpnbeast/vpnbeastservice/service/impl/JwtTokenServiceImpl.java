package com.vpnbeast.vpnbeastservice.service.impl;

import com.vpnbeast.vpnbeastservice.configuration.AuthenticationProperties;
import com.vpnbeast.vpnbeastservice.model.response.JwtResponse;
import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import com.vpnbeast.vpnbeastservice.persistent.service.UserService;
import com.vpnbeast.vpnbeastservice.service.JwtTokenService;
import com.vpnbeast.vpnbeastservice.util.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final AuthenticationProperties authenticationProperties;
    private final UserService userService;

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        final boolean isExpired = expiration.before(new Date());
        final String userName = getUsernameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isExpired);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public ArrayList getRolesFromToken(String token) {
        Claims jwsMap = Jwts.parser().setSigningKey(getPublicKey(authenticationProperties.getPublicKeyString()))
                .parseClaimsJws(token)
                .getBody();
        return jwsMap.get("roles", ArrayList.class);
    }

    @Override
    public LocalDateTime getExpiresAt(String token) {
        return DateUtil.convertDateToLocalDateTime(getClaimFromToken(token, Claims::getExpiration));
    }

    //for retrieveing any information from token we will need the secret key
    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(getPublicKey(authenticationProperties.getPublicKeyString()))
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    @Override
    public JwtResponse generateTokenResponse(UserDetails userDetails) {
        final String accessToken = generateToken(userDetails, authenticationProperties.getAccessTokenValidInMinutes());
        final String refreshToken = generateToken(userDetails, authenticationProperties.getRefreshTokenValidInMinutes());
        User user = userService.getByUserName(userDetails.getUsername());
        user.setAccessToken(accessToken);
        user.setAccessTokenExpiresAt(getExpiresAt(accessToken));
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiresAt(getExpiresAt(refreshToken));
        return JwtResponse.jwtResponseBuilder()
                .user(user)
                .build();
    }

    private String generateToken(UserDetails userDetails, Long tokenValidInMinutes) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(authenticationProperties.getIssuer())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (tokenValidInMinutes * 60 * 1000)))
                .signWith(SignatureAlgorithm.RS256, getPrivateKey(authenticationProperties.getPrivateKeyString()))
                .compact();
    }

    private RSAPrivateKey getPrivateKey(String key) {
        try {
            byte[] encoded = Base64.getMimeDecoder().decode(key);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            log.error("", exception);
        }
        return null;
    }

    private static RSAPublicKey getPublicKey(String key) {
        try {
            byte[] encoded = Base64.getMimeDecoder().decode(key);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            log.error("", exception);
        }
        return null;
    }

}