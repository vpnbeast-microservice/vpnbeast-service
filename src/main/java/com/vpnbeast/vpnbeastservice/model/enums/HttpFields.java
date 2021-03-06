package com.vpnbeast.vpnbeastservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum  HttpFields {

    TIMESTAMP("timestamp"),
    MESSAGE("message"),
    HTTP_CODE("httpCode"),
    STATUS("status"),
    ERROR_MESSAGE("errorMessage"),
    ERRORS("errors");

    @Getter
    @Setter
    private String field;

}