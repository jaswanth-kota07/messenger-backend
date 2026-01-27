package com.jashu.Messenger.controller;


import com.jashu.Messenger.model.Message;
import com.jashu.Messenger.model.Messagedto;
import com.jashu.Messenger.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class ChatSocketController {

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private MessageService service;

    @MessageMapping("/sendmsg")
    public void sendMessage(Messagedto message){
        Message msg=service.savemsg(message);

        template.convertAndSend("/topic/chat/" + msg.getChatid(), msg);


    }

}
