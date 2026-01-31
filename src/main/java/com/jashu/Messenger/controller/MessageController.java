package com.jashu.Messenger.controller;


import com.jashu.Messenger.model.Messagedto;
import com.jashu.Messenger.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://whatsapp-clone-snowy.vercel.app")
@RequestMapping("/api")
public class MessageController {

    @Autowired
    MessageService service;

    @PostMapping("/sendmsg")
    public ResponseEntity<?> sendMessage(@RequestBody Messagedto message){
        return service.sendMessage(message);
    }

    @GetMapping("/getmsgs/{chatid}")
    public ResponseEntity<?> getMessages(@PathVariable int chatid){

        return service.getMessages(chatid);
    }

}
