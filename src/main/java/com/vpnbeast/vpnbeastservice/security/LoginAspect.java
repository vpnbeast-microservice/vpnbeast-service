package com.vpnbeast.vpnbeastservice.security;

import com.vpnbeast.vpnbeastservice.model.response.JwtResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoginAspect {

    private final LoginFailureEventPublisher failureEventPublisher;
    private final LoginSuccessEventPublisher successEventPublisher;
    private final TokenSuccessEventPublisher tokenSuccessEventPublisher;

    @Pointcut("execution(* org.springframework.security.authentication.AuthenticationProvider.authenticate(..))")
    public void doAuthenticate(){
        // This method is the pointcut of authentication
    }

    // below method will throw and event and it will update tokens for user
    @AfterReturning(pointcut="execution(* com.vpnbeast.vpnbeastservice.service.impl.JwtTokenServiceImpl.generateTokenResponse(..))",
            returning="jwtResponse")
    public void afterTokenResponseGenerated(JwtResponse jwtResponse) {
        tokenSuccessEventPublisher.publish(new TokenSuccessEvent(jwtResponse));
    }

    // It will fetch the Authentication object returned from org.springframework.security.authentication.AuthenticationProvider.authenticate method
    // See line 19
    @AfterReturning(value = "com.vpnbeast.vpnbeastservice.security.LoginAspect.doAuthenticate()",
            returning = "authentication")
    public void logAfterAuthenticate(Authentication authentication){
        /*
        Below line will publish an LoginSuccessEvent custom event to Spring event system. Listener will catch that event
        and will do something with that event.
        Published custom events will go through separate threads and we dont need to worry about it anymore. The class who
        publishing the event does not need to know, nor care about the event.
        */
        successEventPublisher.publish(new LoginSuccessEvent(authentication));
    }

    // args(authentication) is going to bind Authentication object on the method signature to the method
    @AfterThrowing("com.vpnbeast.vpnbeastservice.security.LoginAspect.doAuthenticate() && args(authentication)")
    public void logAuthenicationException(Authentication authentication){
        /*
        Below line will publish an LoginFailureEvent custom event to Spring event system. Listener will catch that event
        and will do something with that event.
        Published custom events will go through separate threads and we dont need to worry about it anymore. The class who
        publishing the event does not need to know, nor care about the event.
        */
        failureEventPublisher.publish(new LoginFailureEvent(authentication));
    }
}