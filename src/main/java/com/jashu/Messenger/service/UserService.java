package com.jashu.Messenger.service;


import com.jashu.Messenger.model.User;
import com.jashu.Messenger.model.UserDTO;
import com.jashu.Messenger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository repo;

    public void saveUser(UserDTO user) {
        if(!repo.existsByUser(user.getUser())) {
            User users = new User(user.getUser(), user.getPassword());
            repo.save(users);
        }else{
            throw new RuntimeException("Username already exists");
        }
    }

    public ResponseEntity<?> verifyUser(UserDTO user) {
        User dbUser = repo.getDetailsByUser(user.getUser());

        if (dbUser == null || !dbUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(dbUser);
    }
}
