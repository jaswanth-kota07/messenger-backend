package com.jashu.Messenger.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "blocks")
@Getter
@Setter
@NoArgsConstructor
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private long blockIndex;

    @Column(nullable = false)
    private UUID messageId;

    @Column(nullable = false, length = 64)
    private String messageHash;

    @Column(nullable = false, length = 64)
    private String previousHash;

    @Column(nullable = false, length = 64)
    private String currentHash;

    @Column(nullable = false)
    private long timestampMillis;

    public Block(long blockIndex, UUID messageId, String messageHash, String previousHash, String currentHash, long timestampMillis) {
        this.blockIndex = blockIndex;
        this.messageId = messageId;
        this.messageHash = messageHash;
        this.previousHash = previousHash;
        this.currentHash = currentHash;
        this.timestampMillis = timestampMillis;
    }
}
