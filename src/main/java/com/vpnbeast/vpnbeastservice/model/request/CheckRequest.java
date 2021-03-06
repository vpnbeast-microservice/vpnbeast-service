package com.vpnbeast.vpnbeastservice.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CheckRequest {

    private String plainText;
    private String encryptedText;

}