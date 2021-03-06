package com.vpnbeast.vpnbeastservice.client;

import com.vpnbeast.vpnbeastservice.model.request.EmailRequest;
import com.vpnbeast.vpnbeastservice.model.response.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "email-service", url = "${vpnbeast-service.client.email-service.listOfServers}")
public interface EmailServiceClient {

    @PostMapping(value = "/email-controller/send-email")
    EmailResponse sendEmail(@RequestBody EmailRequest emailRequest);

}