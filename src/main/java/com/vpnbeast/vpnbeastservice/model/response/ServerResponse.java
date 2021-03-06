package com.vpnbeast.vpnbeastservice.model.response;

import com.vpnbeast.vpnbeastservice.persistent.entity.Server;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerResponse extends BaseEntityResponse {

    private String hostname;
    private String ip;
    private String proto;
    private String confData;
    private String countryLong;
    private Integer port;
    private Boolean enabled;
    private Long speed;
    private Long numVpnSessions;
    private Long ping;

    @Builder(builderMethodName = "serverResponseBuilder")
    public ServerResponse(Server server) {
        super(server.getUuid(), server.getId(), server.getCreatedAt(), server.getUpdatedAt(), server.getVersion());
        this.hostname = server.getHostname();
        this.proto = server.getProto();
        this.port = server.getPort();
        this.confData = server.getConfData();
        this.ip = server.getIp();
        this.countryLong = server.getCountryLong();
        this.speed = server.getSpeed();
        this.numVpnSessions = server.getNumVpnSessions();
        this.ping = server.getPing();
        this.enabled = server.getEnabled();
    }

}