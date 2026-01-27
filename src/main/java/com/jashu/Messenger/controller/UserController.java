package com.jashu.Messenger.controller;


import com.jashu.Messenger.model.UserDTO;
import com.jashu.Messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService service;

    @PostMapping("/signup")
    public ResponseEntity<?> userSignup(@RequestBody UserDTO user) {
        try {
            service.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("user created successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserDTO user) {
        return service.verifyUser(user);
    }


}
