package com.jashu.Messenger.service;


import com.jashu.Messenger.dto.MessageRequest;
import com.jashu.Messenger.model.Message;
import com.jashu.Messenger.repository.ChatRepository;
import com.jashu.Messenger.repository.MessageRepository;
import com.jashu.Messenger.exceptions.ResourceNotFoundException;
import com.jashu.Messenger.exceptions.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    public Message sendMessage(MessageRequest message, UUID authenticatedUserId) {
        log.info("Sending message from user: {} to user: {} in chat: {}", message.getSenderId(), message.getReceiverId(), message.getChatId());
        if (!isAllowedToSend(message, authenticatedUserId)) {
            log.warn("Unauthorized message attempt by user: {} in chat: {}", authenticatedUserId, message.getChatId());
            throw new UnauthorizedAccessException("Cannot send messages as another user");
        }
        return messageRepository.save(toEntity(message));
    }

    public List<Message> getMessages(UUID chatId, UUID authenticatedUserId) {
        log.info("Fetching messages for chat: {} by user: {}", chatId, authenticatedUserId);
        if (!isChatParticipant(chatId, authenticatedUserId)) {
            log.warn("Unauthorized access attempt to chat: {} by user: {}", chatId, authenticatedUserId);
            throw new UnauthorizedAccessException("Cannot access this chat");
        }
        return messageRepository.getMessagesByChatId(chatId);
    }

    public Message saveMessage(MessageRequest message, UUID authenticatedUserId) {
        log.info("Saving message from user: {} to user: {} in chat: {}", message.getSenderId(), message.getReceiverId(), message.getChatId());
        if (!isAllowedToSend(message, authenticatedUserId)) {
            log.warn("Unauthorized message save attempt by user: {} in chat: {}", authenticatedUserId, message.getChatId());
            throw new UnauthorizedAccessException("Cannot send messages as another user");
        }
        Message msg = toEntity(message);
        return messageRepository.save(msg);
    }

    private boolean isAllowedToSend(MessageRequest message, UUID authenticatedUserId) {
        return message.getSenderId().equals(authenticatedUserId)
                && isChatParticipant(message.getChatId(), authenticatedUserId)
                && isChatParticipant(message.getChatId(), message.getReceiverId());
    }

    private Message toEntity(MessageRequest message) {
        return new Message(message.getMessage(), message.getChatId(), message.getSenderId(), message.getReceiverId());
    }

    private boolean isChatParticipant(UUID chatId, UUID userId) {
        return chatRepository.findById(chatId)
                .map(chat -> chat.getUser1Id().equals(userId) || chat.getUser2Id().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
    }
}
