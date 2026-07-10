package com.jashu.Messenger.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "chats")
@Setter
@Getter
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID chatId;

    @Column(nullable = false)
    @NotNull
    private UUID user1Id;

    @Column(nullable = false)
    @NotNull
    private UUID user2Id;

    public Chat(UUID user1Id, UUID user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }
}
