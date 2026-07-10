package com.jashu.Messenger.service;

import com.jashu.Messenger.util.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class EncryptionService {

    private final SecretKey aesSecretKey;

    public String encrypt(String plainText) {
        return AESUtil.encrypt(plainText, aesSecretKey);
    }

    public String decrypt(String cipherText) {
        return AESUtil.decrypt(cipherText, aesSecretKey);
    }
}
