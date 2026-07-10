package com.jashu.Messenger.service;

import com.jashu.Messenger.dto.ChatSummary;
import com.jashu.Messenger.exceptions.BadRequestException;
import com.jashu.Messenger.exceptions.ResourceAlreadyExistsException;
import com.jashu.Messenger.exceptions.ResourceNotFoundException;
import com.jashu.Messenger.model.Chat;
import com.jashu.Messenger.model.User;
import com.jashu.Messenger.repository.ChatRepository;
import com.jashu.Messenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public List<ChatSummary> getAllChatsById(UUID userId) {
        log.info("Fetching all chats for user: {}", userId);
        List<ChatSummary> chats = chatRepository.findChatsByUserId(userId);
        if (!chats.isEmpty()) {
            log.info("Found {} chats for user: {}", chats.size(), userId);
            return chats;
        } else {
            log.warn("No chats found for user: {}", userId);
            throw new ResourceNotFoundException("No chats found");
        }
    }

    public List<ChatSummary> getAllChatsBySearch(UUID userId, String keyword) {
        log.info("Searching chats for user: {} with keyword: {}", userId, keyword);
        List<ChatSummary> chats = chatRepository.searchChatsByOtherUser(userId, keyword.trim());
        if (!chats.isEmpty()) {
            log.info("Found {} search results for user: {} with keyword: {}", chats.size(), userId, keyword);
            return chats;
        } else {
            log.warn("No search results found for user: {} with keyword: {}", userId, keyword);
            throw new ResourceNotFoundException("No chats found");
        }
    }

    public void createChat(String otherUser, UUID userId) {
        log.info("Attempting to create chat between user: {} and other user: {}", userId, otherUser);
        User user = userRepository.findByUsernameIgnoreCase(otherUser.trim())
                .orElseThrow(() -> {
                    log.warn("Create chat failed: Other user '{}' not found", otherUser);
                    return new ResourceNotFoundException("User not found");
                });

        if (user.getId().equals(userId)) {
            log.warn("Create chat failed: User: {} attempted to chat with themselves", userId);
            throw new BadRequestException("Cannot chat with yourself");
        }
        Chat chatExist = chatRepository.findChatBetweenUsers(userId, user.getId());
        if (chatExist != null) {
            log.warn("Create chat failed: Chat already exists between user: {} and user: {}", userId, user.getId());
            throw new ResourceAlreadyExistsException("Chat already exists");
        } else {
            Chat chat = new Chat(userId, user.getId());
            chatRepository.save(chat);
            log.info("Chat created successfully between user: {} and user: {}", userId, user.getId());
        }
    }
}
