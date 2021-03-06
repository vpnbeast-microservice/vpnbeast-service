package com.vpnbeast.vpnbeastservice.persistent.entity;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "servers")
public class Server extends BaseEntity {

    private String uuid;
    private String hostname;
    private String ip;
    private String confData;
    private String proto;
    private String countryShort;
    private String countryLong;
    private Integer port;
    private Boolean enabled;
    private Long score;
    private Long ping;
    private Long speed;
    private Long numVpnSessions;
    private Long uptime;
    private Long totalUsers;
    private Long totalTraffic;

}