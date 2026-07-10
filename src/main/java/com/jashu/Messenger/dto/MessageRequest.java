package com.jashu.Messenger.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    @NotBlank
    @Size(max = 2000)
    private String message;

    @NotNull
    @JsonAlias("senderid")
    private UUID senderId;

    @NotNull
    @JsonAlias("receiverid")
    private UUID receiverId;

    @NotNull
    @JsonAlias("chatid")
    private UUID chatId;
}
