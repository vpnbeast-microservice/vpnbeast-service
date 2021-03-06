package com.vpnbeast.vpnbeastservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VerificationCodeNotValidException extends RuntimeException {

    private final transient ExceptionInfo exceptionInfo;

}