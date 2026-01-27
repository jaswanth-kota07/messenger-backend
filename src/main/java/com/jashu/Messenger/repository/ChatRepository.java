package com.jashu.Messenger.repository;

import com.jashu.Messenger.model.Chat;
import com.jashu.Messenger.model.Chatdto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Integer> {


    @Query("""
SELECT new com.jashu.Messenger.model.Chatdto(
    c.chatid,
    CASE
        WHEN c.user1id = :userid THEN u2.user
        ELSE u1.user
    END,
    CASE
        WHEN c.user1id = :userid THEN u2.id
        ELSE u1.id
    END
)
FROM Chat c
JOIN User u1 ON u1.id = c.user1id
JOIN User u2 ON u2.id = c.user2id
WHERE c.user1id = :userid
   OR c.user2id = :userid
""")
    List<Chatdto> findChatsByUserId(@Param("userid") int userid);

    @Query("""
SELECT new com.jashu.Messenger.model.Chatdto(
    c.chatid,
    CASE
        WHEN c.user1id = :userid THEN u2.user
        ELSE u1.user
    END,
    CASE
        WHEN c.user1id = :userid THEN u2.id
        ELSE u1.id
    END
)
FROM Chat c
JOIN User u1 ON u1.id = c.user1id
JOIN User u2 ON u2.id = c.user2id
WHERE (c.user1id = :userid OR c.user2id = :userid)
  AND (
        CASE
            WHEN c.user1id = :userid THEN u2.user
            ELSE u1.user
        END
      ) LIKE %:keyword%
""")
    List<Chatdto> searchChatsByOtherUser(
            @Param("userid") int userid,
            @Param("keyword") String keyword
    );

    @Query("""
        SELECT c 
        FROM Chat c
        WHERE (c.user1id = :user1 AND c.user2id = :user2)
           OR (c.user1id = :user2 AND c.user2id = :user1)
    """)
    public Chat findChatBetweenUsers(@Param("user1") int user1id,
                                        @Param("user2") int user2id);


}
