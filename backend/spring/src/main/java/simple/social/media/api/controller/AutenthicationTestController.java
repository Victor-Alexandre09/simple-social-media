package simple.social.media.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import simple.social.media.api.domain.User.User;
import simple.social.media.api.domain.User.UserRepository;

import java.util.List;

@RestController
public class AutenthicationTestController {

    @Autowired
    UserRepository repository;

    @GetMapping("/Hello")
    public ResponseEntity<String> Hello () {
        return ResponseEntity.ok().body("Hello, World!");
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<User>> GetUsers () {
        var usersList = repository.findAll();
        return ResponseEntity.ok(usersList);
    }
}
