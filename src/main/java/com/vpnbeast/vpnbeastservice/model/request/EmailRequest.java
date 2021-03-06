package com.vpnbeast.vpnbeastservice.model.request;

import com.vpnbeast.vpnbeastservice.model.enums.EmailType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailRequest {

    private EmailType emailType;
    private Integer verificationCode;
    private String emailAddress;

}