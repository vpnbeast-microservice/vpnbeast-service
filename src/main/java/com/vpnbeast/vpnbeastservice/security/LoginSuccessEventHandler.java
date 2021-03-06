package com.vpnbeast.vpnbeastservice.security;

import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import com.vpnbeast.vpnbeastservice.persistent.service.UserService;
import com.vpnbeast.vpnbeastservice.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessEventHandler implements ApplicationListener<LoginSuccessEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(LoginSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        log.info("LoginEvent Success for: {}", authentication.getPrincipal());
        updateUserAccount(authentication);
    }

    private void updateUserAccount(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByUserName(userDetails.getUsername());
        if (Objects.nonNull(user)) {
            log.info("Login succeeded, resetting failed attempts and last login for user {}", user.getUserName());
            user.setFailedLoginAttempts(0);
            user.setLastLogin(DateUtil.getCurrentLocalDateTime());
            if (!user.getEnabled())
                user.setEnabled(true);
            userService.update(user);
        }
    }

}