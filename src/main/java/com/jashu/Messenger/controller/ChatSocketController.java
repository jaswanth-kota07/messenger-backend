package com.jashu.Messenger.controller;

import com.jashu.Messenger.dto.MessageRequest;
import com.jashu.Messenger.dto.MessageResponse;
import com.jashu.Messenger.security.UserPrincipal;
import com.jashu.Messenger.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatSocketController {

    private final SimpMessagingTemplate template;
    private final MessageService service;

    @MessageMapping("/sendmsg")
    public void sendMessage(@Valid MessageRequest message, Principal principal) {
        log.info("Received WebSocket message via /sendmsg from principal: {}", principal != null ? principal.getName() : "null");
        if (!(principal instanceof Authentication authentication)
                || !(authentication.getPrincipal() instanceof UserPrincipal userPrincipal)) {
            log.warn("Access denied for WebSocket message: principal is not authenticated properly");
            throw new AccessDeniedException("Unauthorized websocket message");
        }

        MessageResponse msg = service.saveMessage(message, userPrincipal.getId());
        log.info("Successfully saved message ID: {} and broadcasting to topic: /topic/chat/{}", msg.getId(), msg.getChatId());

        template.convertAndSend("/topic/chat/" + msg.getChatId(), msg);
    }
}
