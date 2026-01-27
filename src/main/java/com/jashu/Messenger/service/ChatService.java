package com.jashu.Messenger.service;


import com.jashu.Messenger.model.Chat;
import com.jashu.Messenger.model.Chatdto;
import com.jashu.Messenger.model.User;
import com.jashu.Messenger.repository.ChatRepository;
import com.jashu.Messenger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    @Autowired
    ChatRepository repo;
    @Autowired
    UserRepository userRepo;

    public ResponseEntity<?> getAllChatsById(int userid) {

        List<Chatdto> chats=repo.findChatsByUserId(userid);
        if(!chats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(chats);
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No chats found");
        }
    }

    public ResponseEntity<?> getAllChatsBySearch(int userid, String keyword) {
        List<Chatdto> chats=repo.searchChatsByOtherUser(userid,keyword);
        if(!chats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(chats);
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No chats found");
        }
    }

    public ResponseEntity<?> createChat(String otheruser, int userid) {
        User user=userRepo.findByUser(otheruser);
        if(user==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if(user.getId() == userid) {
            return ResponseEntity.badRequest().body("Cannot chat with yourself");
        }
        Chat chatExist =repo.findChatBetweenUsers(userid,user.getId());
        if(chatExist!=null) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Chat already exists");
        }else{
            Chat chat=new Chat(userid,user.getId());
            repo.save(chat);
            return ResponseEntity.status(HttpStatus.CREATED).body("chat created successfully");
        }

    }


}
