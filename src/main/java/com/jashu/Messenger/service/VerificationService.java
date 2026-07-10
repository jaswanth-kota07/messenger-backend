package com.jashu.Messenger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final BlockchainService blockchainService;

    public boolean verifyMessage(UUID messageId, String cipherText) {
        return blockchainService.verifyMessage(messageId, cipherText);
    }
}
