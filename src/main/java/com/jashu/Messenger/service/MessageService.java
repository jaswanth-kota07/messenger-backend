package com.jashu.Messenger.service;

import com.jashu.Messenger.dto.MessageDetailResponse;
import com.jashu.Messenger.dto.MessageRequest;
import com.jashu.Messenger.dto.MessageResponse;
import com.jashu.Messenger.dto.VerificationResponse;
import com.jashu.Messenger.exceptions.ResourceNotFoundException;
import com.jashu.Messenger.exceptions.UnauthorizedAccessException;
import com.jashu.Messenger.model.Message;
import com.jashu.Messenger.repository.BlockRepository;
import com.jashu.Messenger.repository.ChatRepository;
import com.jashu.Messenger.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final EncryptionService encryptionService;
    private final BlockchainService blockchainService;
    private final VerificationService verificationService;
    private final BlockRepository blockRepository;

    @Transactional
    public MessageResponse sendMessage(MessageRequest message, UUID authenticatedUserId) {
        log.info("Sending message from user: {} to user: {} in chat: {}", message.getSenderId(), message.getReceiverId(), message.getChatId());
        if (!isAllowedToSend(message, authenticatedUserId)) {
            log.warn("Unauthorized message attempt by user: {} in chat: {}", authenticatedUserId, message.getChatId());
            throw new UnauthorizedAccessException("Cannot send messages as another user");
        }
        return saveAndProcessMessage(message);
    }

    public List<MessageResponse> getMessages(UUID chatId, UUID authenticatedUserId) {
        log.info("Fetching messages for chat: {} by user: {}", chatId, authenticatedUserId);
        if (!isChatParticipant(chatId, authenticatedUserId)) {
            log.warn("Unauthorized access attempt to chat: {} by user: {}", chatId, authenticatedUserId);
            throw new UnauthorizedAccessException("Cannot access this chat");
        }
        return messageRepository.getMessagesByChatId(chatId)
                .stream()
                .map(this::toVerifiedResponse)
                .toList();
    }

    @Transactional
    public MessageResponse saveMessage(MessageRequest message, UUID authenticatedUserId) {
        log.info("Saving message from user: {} to user: {} in chat: {}", message.getSenderId(), message.getReceiverId(), message.getChatId());
        if (!isAllowedToSend(message, authenticatedUserId)) {
            log.warn("Unauthorized message save attempt by user: {} in chat: {}", authenticatedUserId, message.getChatId());
            throw new UnauthorizedAccessException("Cannot send messages as another user");
        }
        return saveAndProcessMessage(message);
    }

    public MessageDetailResponse getMessageDetails(UUID messageId, UUID authenticatedUserId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if (!isChatParticipant(message.getChatId(), authenticatedUserId)) {
            throw new UnauthorizedAccessException("Cannot access this message");
        }

        boolean verified = verificationService.verifyMessage(message.getId(), message.getMessage());
        String hash = blockRepository.findByMessageId(message.getId())
                .map(block -> block.getMessageHash())
                .orElse(null);

        return new MessageDetailResponse(
                message.getId(),
                encryptionService.decrypt(message.getMessage()),
                message.getMessage(),
                message.getSenderId(),
                message.getReceiverId(),
                message.getCreatedAt(),
                hash,
                verified
        );
    }

    public VerificationResponse verifyMessage(UUID messageId, UUID authenticatedUserId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if (!isChatParticipant(message.getChatId(), authenticatedUserId)) {
            throw new UnauthorizedAccessException("Cannot access this message");
        }

        boolean verified = verificationService.verifyMessage(message.getId(), message.getMessage());
        return new VerificationResponse(verified);
    }

    private MessageResponse saveAndProcessMessage(MessageRequest message) {
        String cipherText = encryptionService.encrypt(message.getMessage());
        Message entity = new Message(cipherText, message.getChatId(), message.getSenderId(), message.getReceiverId());
        Message saved = messageRepository.save(entity);
        blockchainService.createBlock(saved.getId(), saved.getMessage());

        return new MessageResponse(
                saved.getId(),
                message.getMessage(),
                saved.getChatId(),
                saved.getSenderId(),
                saved.getReceiverId(),
                true
        );
    }

    private MessageResponse toVerifiedResponse(Message message) {
        boolean verified = verificationService.verifyMessage(message.getId(), message.getMessage());
        String plainText = encryptionService.decrypt(message.getMessage());

        return new MessageResponse(
                message.getId(),
                plainText,
                message.getChatId(),
                message.getSenderId(),
                message.getReceiverId(),
                verified
        );
    }

    private boolean isAllowedToSend(MessageRequest message, UUID authenticatedUserId) {
        return message.getSenderId().equals(authenticatedUserId)
                && isChatParticipant(message.getChatId(), authenticatedUserId)
                && isChatParticipant(message.getChatId(), message.getReceiverId());
    }

    private boolean isChatParticipant(UUID chatId, UUID userId) {
        return chatRepository.findById(chatId)
                .map(chat -> chat.getUser1Id().equals(userId) || chat.getUser2Id().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
    }
}
