package com.jashu.Messenger.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 2000)
    @NotBlank
    @Size(max = 2000)
    private String message;

    @Column(nullable = false)
    @NotNull
    @JsonProperty("chatid")
    private UUID chatId;

    @Column(nullable = false)
    @NotNull
    @JsonProperty("senderid")
    private UUID senderId;

    @Column(nullable = false)
    @NotNull
    @JsonProperty("receiverid")
    private UUID receiverId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public Message(String message, UUID chatId, UUID senderId, UUID receiverId) {
        this.message = message;
        this.chatId = chatId;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
