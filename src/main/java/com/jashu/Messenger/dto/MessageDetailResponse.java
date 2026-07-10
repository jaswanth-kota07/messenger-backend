package com.jashu.Messenger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDetailResponse {

    private UUID id;
    private String message;
    private String encryptedData;

    @JsonProperty("senderid")
    private UUID senderId;

    @JsonProperty("receiverid")
    private UUID receiverId;

    private Instant timestamp;
    private String hash;
    private boolean verified;
}
