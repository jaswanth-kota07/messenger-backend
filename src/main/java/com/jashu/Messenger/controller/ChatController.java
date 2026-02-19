package com.jashu.Messenger.controller;


import com.jashu.Messenger.model.Chat;
import com.jashu.Messenger.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"https://whatsapp-clone-snowy.vercel.app","http://localhost:5173"})
@RequestMapping("/api")
public class ChatController {
    @Autowired
    ChatService service;
    @GetMapping("/chats/{userid}" )
    public ResponseEntity<?> getAllChatsById(@PathVariable int userid){
        return service.getAllChatsById(userid);
    }
    @GetMapping("/search/{userid}" )
    public ResponseEntity<?> getChatsBySearch(@PathVariable int userid ,@RequestParam String keyword){
        return service.getAllChatsBySearch(userid,keyword);
    }

    @PostMapping("/createchat/{userid}")
    public ResponseEntity<?> createChat(@RequestParam String otheruser,@PathVariable int userid){
        return service.createChat(otheruser,userid);
    }
}
