package com.jashu.Messenger.service;


import com.jashu.Messenger.model.Message;
import com.jashu.Messenger.model.Messagedto;
import com.jashu.Messenger.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    MessageRepository repo;

    public ResponseEntity<?> sendMessage(Messagedto message) {
        Message mess=new Message(message.getMessage(), message.getChatid(), message.getSenderid(), message.getReceiverid());
        repo.save(mess);
        return ResponseEntity.status(HttpStatus.OK).body("Message sent successfully");
    }

    public ResponseEntity<?> getMessages(int chatid) {
        return ResponseEntity.status(HttpStatus.OK).body(repo.getMessagesByChatId(chatid));
    }

    public Message savemsg(Messagedto message) {
        Message msg=new Message(message.getMessage(), message.getChatid(), message.getSenderid(), message.getReceiverid());
        return repo.save(msg);
    }
}
