package com.vpnbeast.vpnbeastservice.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncryptResponse {

    private String tag;
    private String output;

}