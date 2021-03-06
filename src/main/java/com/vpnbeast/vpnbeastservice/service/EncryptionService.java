package com.vpnbeast.vpnbeastservice.service;

public interface EncryptionService {

    String encrypt(String plainText);
    Boolean check(String plainText, String encryptedText);

}