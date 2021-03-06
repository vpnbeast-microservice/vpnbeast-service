package com.vpnbeast.vpnbeastservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenExpiredException extends RuntimeException {

    private final transient ExceptionInfo exceptionInfo;

}