package com.vpnbeast.vpnbeastservice.security;

import org.springframework.context.ApplicationEvent;

public class TokenSuccessEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     * @param source the object on which the event initially occurred (never {@code null})
     **/
    public TokenSuccessEvent(Object source) {
        super(source);
    }

}