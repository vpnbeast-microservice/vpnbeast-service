package com.vpnbeast.vpnbeastservice.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FailureResponse {

    private String errorMessage;
    private Boolean status;

}
