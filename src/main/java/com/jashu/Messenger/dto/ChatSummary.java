package com.jashu.Messenger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ChatSummary {

    @NotNull
    @JsonProperty("chatid")
    private UUID chatId;

    @NotBlank
    @Size(min = 3, max = 30)
    @JsonProperty("otheruser")
    private String otherUser;

    @NotNull
    @JsonProperty("otheruserid")
    private UUID otherUserId;
}
