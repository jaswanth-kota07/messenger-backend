package com.jashu.Messenger.controller;

import com.jashu.Messenger.dto.MessageRequest;
import com.jashu.Messenger.dto.MessageDetailResponse;
import com.jashu.Messenger.dto.MessageResponse;
import com.jashu.Messenger.security.UserPrincipal;
import com.jashu.Messenger.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService service;

    @PostMapping("/sendmsg")
    public ResponseEntity<?> sendMessage(@Valid @RequestBody MessageRequest message, @AuthenticationPrincipal UserPrincipal principal) {
        log.info("HTTP request to send message from user: {} to user: {} in chat: {} by user: {}", message.getSenderId(), message.getReceiverId(), message.getChatId(), principal.getUsername());
        return ResponseEntity.ok(service.sendMessage(message, principal.getId()));
     }

    @GetMapping("/getmsgs/{chatId}")
    public ResponseEntity<?> getMessages(@PathVariable UUID chatId, @AuthenticationPrincipal UserPrincipal principal) {
        log.info("HTTP request to get messages for chat ID: {} by user: {}", chatId, principal.getUsername());
        return ResponseEntity.ok(service.getMessages(chatId, principal.getId()));
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<MessageDetailResponse> getMessageDetails(
            @PathVariable UUID messageId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        log.info("HTTP request to get message details for ID: {} by user: {}", messageId, principal.getUsername());
        return ResponseEntity.ok(service.getMessageDetails(messageId, principal.getId()));
    }

}
