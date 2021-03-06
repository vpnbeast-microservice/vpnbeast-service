package com.vpnbeast.vpnbeastservice.client;

import com.vpnbeast.vpnbeastservice.model.request.CheckRequest;
import com.vpnbeast.vpnbeastservice.model.request.EncryptRequest;
import com.vpnbeast.vpnbeastservice.model.response.CheckResponse;
import com.vpnbeast.vpnbeastservice.model.response.EncryptResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "encryption-service", url = "${vpnbeast-service.client.encryption-service.listOfServers}")
public interface EncryptionServiceClient {

    @PostMapping(value = "/encryption-controller/encrypt")
    EncryptResponse encryptPassword(@RequestBody EncryptRequest encryptRequest);

    @PostMapping(value = "/encryption-controller/check")
    CheckResponse checkPassword(@RequestBody CheckRequest encryptRequest);

}