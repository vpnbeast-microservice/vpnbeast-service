package com.vpnbeast.vpnbeastservice.model.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class EmailResponse {

    private String tag;
    private Boolean status;
    private LocalDateTime timestamp;

}