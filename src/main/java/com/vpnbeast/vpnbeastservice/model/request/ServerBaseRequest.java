package com.vpnbeast.vpnbeastservice.model.request;

import lombok.*;
import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerBaseRequest {

    private Long id;

    @NotNull(message = "Hostname must not be null!")
    @Size(min = 3, max = 40, message = "hostname must be between 3 and 40 characters")
    private String hostname;

    @NotNull(message = "IP address must not be null!")
    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$",
            message = "Not a valid IP address format!")
    private String ip;

    @NotNull(message = "Protocol must not be null!")
    @Pattern(regexp = "tcp|udp", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Protocol must be tcp or udp!")
    private String proto;

    @NotNull(message = "Port must not be null!")
    @Min(value = 80, message = "Port must not be less than 80!")
    @Max(value = 65535, message = "Port should not be greater than 65535!")
    private Integer port;

}