package com.jashu.Messenger.controller;

import com.jashu.Messenger.exceptions.BadRequestException;
import com.jashu.Messenger.exceptions.UnauthorizedAccessException;
import com.jashu.Messenger.security.UserPrincipal;
import com.jashu.Messenger.service.ChatService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService service;

    @GetMapping("/chats/{userId}")
    public ResponseEntity<?> getAllChatsById(@PathVariable UUID userId, @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Request to get all chats for user ID: {} from authenticated user: {}", userId, principal.getUsername());
        if (!principal.getId().equals(userId)) {
            log.warn("Access denied: User {} tried to access chats of {}", principal.getId(), userId);
            throw new UnauthorizedAccessException("Cannot access another user's chats");
        }
        return ResponseEntity.ok(service.getAllChatsById(userId));
     }

    @GetMapping("/search/{userId}")
    public ResponseEntity<?> getChatsBySearch(@PathVariable UUID userId, @RequestParam @NotBlank String keyword, @AuthenticationPrincipal UserPrincipal principal) {
        log.info("Request to search chats for user ID: {} with keyword: '{}' from authenticated user: {}", userId, keyword, principal.getUsername());
        if (!principal.getId().equals(userId)) {
            log.warn("Access denied: User {} tried to search chats of {}", principal.getId(), userId);
            throw new UnauthorizedAccessException("Cannot access another user's chats");
        }
        return ResponseEntity.ok(service.getAllChatsBySearch(userId, keyword));
    }

    @PostMapping("/createchat/{userId}")
    public ResponseEntity<?> createChat(
            @RequestParam(required = false) String otherUser,
            @RequestParam(name = "otheruser", required = false) String legacyOtherUser,
            @PathVariable UUID userId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        log.info("Request to create chat for user ID: {} with other user: '{}' (legacy: '{}') from authenticated user: {}", userId, otherUser, legacyOtherUser, principal.getUsername());
        if (!principal.getId().equals(userId)) {
            log.warn("Access denied: User {} tried to create chat for {}", principal.getId(), userId);
            throw new UnauthorizedAccessException("Cannot create chat for another user");
        }

        String requestedOtherUser = otherUser != null ? otherUser : legacyOtherUser;
        if (requestedOtherUser == null || requestedOtherUser.isBlank()) {
            log.warn("Create chat failed: other user parameter is missing or blank");
            throw new BadRequestException("Other user is required");
        }

        service.createChat(requestedOtherUser, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("chat created successfully");
    }
}
