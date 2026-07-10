package com.jashu.Messenger.controller;

import com.jashu.Messenger.dto.ChainValidationResponse;
import com.jashu.Messenger.dto.VerificationResponse;
import com.jashu.Messenger.security.UserPrincipal;
import com.jashu.Messenger.service.BlockchainService;
import com.jashu.Messenger.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/blockchain")
@RequiredArgsConstructor
@Slf4j
public class BlockchainController {

    private final BlockchainService blockchainService;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<?> getAllBlocks() {
        log.info("HTTP request to list all blockchain blocks");
        return ResponseEntity.ok(blockchainService.getAllBlocks());
    }

    @GetMapping("/validate")
    public ResponseEntity<ChainValidationResponse> validateChain() {
        log.info("HTTP request to validate blockchain");
        return ResponseEntity.ok(new ChainValidationResponse(blockchainService.validateChain()));
    }

    @GetMapping("/message/{messageId}")
    public ResponseEntity<?> getBlockByMessage(@PathVariable UUID messageId) {
        log.info("HTTP request to get blockchain block for message: {}", messageId);
        return ResponseEntity.ok(blockchainService.getBlockByMessageId(messageId));
    }

    @GetMapping("/verify/{messageId}")
    public ResponseEntity<VerificationResponse> verifyMessage(
            @PathVariable UUID messageId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        log.info("HTTP request to verify message: {} by user: {}", messageId, principal.getUsername());
        return ResponseEntity.ok(messageService.verifyMessage(messageId, principal.getId()));
    }
}
