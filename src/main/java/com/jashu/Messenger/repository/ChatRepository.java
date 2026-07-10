package com.jashu.Messenger.repository;

import com.jashu.Messenger.dto.ChatSummary;
import com.jashu.Messenger.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    @Query("""
            SELECT new com.jashu.Messenger.dto.ChatSummary(
                c.chatId,
                CASE
                    WHEN c.user1Id = :userId THEN u2.username
                    ELSE u1.username
                END,
                CASE
                    WHEN c.user1Id = :userId THEN u2.id
                    ELSE u1.id
                END
            )
            FROM Chat c
            JOIN User u1 ON u1.id = c.user1Id
            JOIN User u2 ON u2.id = c.user2Id
            WHERE c.user1Id = :userId
               OR c.user2Id = :userId
            """)
    List<ChatSummary> findChatsByUserId(@Param("userId") UUID userId);

    @Query("""
            SELECT new com.jashu.Messenger.dto.ChatSummary(
                c.chatId,
                CASE
                    WHEN c.user1Id = :userId THEN u2.username
                    ELSE u1.username
                END,
                CASE
                    WHEN c.user1Id = :userId THEN u2.id
                    ELSE u1.id
                END
            )
            FROM Chat c
            JOIN User u1 ON u1.id = c.user1Id
            JOIN User u2 ON u2.id = c.user2Id
            WHERE (c.user1Id = :userId OR c.user2Id = :userId)
              AND (
                    CASE
                        WHEN c.user1Id = :userId THEN u2.username
                        ELSE u1.username
                    END
                  ) LIKE %:keyword%
            """)
    List<ChatSummary> searchChatsByOtherUser(@Param("userId") UUID userId, @Param("keyword") String keyword
    );

    @Query("""
                SELECT c 
                FROM Chat c
                WHERE (c.user1Id = :user1 AND c.user2Id = :user2)
                   OR (c.user1Id = :user2 AND c.user2Id = :user1)
            """)
    Chat findChatBetweenUsers(@Param("user1") UUID user1Id, @Param("user2") UUID user2Id);
}
