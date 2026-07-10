package com.jashu.Messenger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private UUID id;
    private String message;

    @JsonProperty("chatid")
    private UUID chatId;

    @JsonProperty("senderid")
    private UUID senderId;

    @JsonProperty("receiverid")
    private UUID receiverId;

    @JsonProperty("verified")
    private boolean verified;
}
