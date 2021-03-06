package com.vpnbeast.vpnbeastservice.security;

import com.vpnbeast.vpnbeastservice.model.response.JwtResponse;
import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import com.vpnbeast.vpnbeastservice.persistent.service.UserService;
import com.vpnbeast.vpnbeastservice.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenSuccessEventHandler implements ApplicationListener<TokenSuccessEvent> {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @Override
    public void onApplicationEvent(TokenSuccessEvent event) {
        JwtResponse response = (JwtResponse) event.getSource();
        updateUserTokens(response);
    }

    private void updateUserTokens(JwtResponse response) {
        User user = userService.getByUserName(jwtTokenService.getUsernameFromToken(response.getAccessToken()));
        if (Objects.nonNull(user)) {
            log.info("Token successfully generated for user {}, updating tokens on DB...", user.getUserName());
            user.setAccessToken(response.getAccessToken());
            user.setAccessTokenExpiresAt(response.getAccessTokenExpiresAt());
            user.setRefreshToken(response.getRefreshToken());
            user.setRefreshTokenExpiresAt(response.getRefreshTokenExpiresAt());
            userService.update(user);
        }
    }

}