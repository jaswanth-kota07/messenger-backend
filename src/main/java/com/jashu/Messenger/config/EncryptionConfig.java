package com.jashu.Messenger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class EncryptionConfig {

    @Bean
    public SecretKey aesSecretKey(@Value("${encryption.secret}") String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != 32) {
            throw new IllegalStateException(
                    "AES_ENCRYPTION_KEY must be exactly 32 characters (256 bits). Current length: " + keyBytes.length
            );
        }
        return new SecretKeySpec(keyBytes, "AES");
    }
}
