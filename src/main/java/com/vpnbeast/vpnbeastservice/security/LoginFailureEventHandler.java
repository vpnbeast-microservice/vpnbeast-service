package com.vpnbeast.vpnbeastservice.security;

import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import com.vpnbeast.vpnbeastservice.persistent.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFailureEventHandler implements ApplicationListener<LoginFailureEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(LoginFailureEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        log.error("LoginEvent failure for: {}", authentication.getPrincipal());
        updateUserAccount(authentication);
    }

    private void updateUserAccount(Authentication authentication) {
        User user = userService.getByUserName((String) authentication.getPrincipal());
        if (user != null) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            log.error("Failed login attempt, setting failedLoginAttempts for user {} as {}", user.getUserName(),
                    user.getFailedLoginAttempts());
            if (user.getFailedLoginAttempts() > 5) {
                log.error("User {} has more than 5 failed login attempt, locking account", user.getUserName());
                user.setEnabled(false);
            }
            userService.update(user);
        }
    }

}