package com.vpnbeast.vpnbeastservice.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
class BaseEntityResponse {

    private String uuid;
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer version;

}