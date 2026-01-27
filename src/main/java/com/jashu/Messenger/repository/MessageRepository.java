package com.jashu.Messenger.repository;

import com.jashu.Messenger.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {


    @Query("SELECT m FROM Message m WHERE m.chatid = :chatid")
    List<Message> getMessagesByChatId(@Param("chatid") int chatid);
}
