package com.vpnbeast.vpnbeastservice.exception;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ExceptionInfo {

    private final String tag;
    private final String errorMessage;
    private final Boolean status;
    private final Integer httpCode;
    private final LocalDateTime timestamp;

}
