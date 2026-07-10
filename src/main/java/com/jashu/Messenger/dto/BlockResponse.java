package com.jashu.Messenger.dto;

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
public class BlockResponse {

    private UUID id;
    private long blockIndex;
    private UUID messageId;
    private String messageHash;
    private String previousHash;
    private String currentHash;
    private Instant timestamp;
}
