package com.vpnbeast.vpnbeastservice.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SuccessResponse {

    private String tag;
    private Boolean status;

}