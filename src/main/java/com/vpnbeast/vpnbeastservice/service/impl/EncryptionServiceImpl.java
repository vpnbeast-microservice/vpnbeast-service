package com.vpnbeast.vpnbeastservice.service.impl;

import com.vpnbeast.vpnbeastservice.client.EncryptionServiceClient;
import com.vpnbeast.vpnbeastservice.model.request.CheckRequest;
import com.vpnbeast.vpnbeastservice.model.request.EncryptRequest;
import com.vpnbeast.vpnbeastservice.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    private EncryptionServiceClient encryptionServiceClient;

    @Autowired
    public void setEncryptionServiceClient(EncryptionServiceClient encryptionServiceClient) {
        this.encryptionServiceClient = encryptionServiceClient;
    }

    @Override
    public String encrypt(String plainText) {
        final EncryptRequest request = EncryptRequest.builder()
                .plainText(plainText)
                .build();
        return encryptionServiceClient.encryptPassword(request).getOutput();
    }

    @Override
    public Boolean check(String plainText, String encryptedText) {
        final CheckRequest request = CheckRequest.builder()
                .plainText(plainText)
                .encryptedText(encryptedText)
                .build();
        return encryptionServiceClient.checkPassword(request).getStatus();
    }

}