package com.vpnbeast.vpnbeastservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@RequiredArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NonUpdatableColumnException extends RuntimeException {

    private final transient ExceptionInfo exceptionInfo;

}
