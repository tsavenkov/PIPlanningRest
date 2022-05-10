package com.volleybe.security.contollers;

import com.volleybe.security.model.User;
import com.volleybe.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserController {
    @Autowired
    UserRepository repository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/findAllUsers")
    public List<User> findAll() {
        return repository.findAll();
    }

    @RequestMapping("/search/{id}")
    public String search(@PathVariable long id) {
        return repository.findById(id).toString();
    }

    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public User create(@RequestBody User user) {

        User a = repository.save(User.builder() .id(user.getId())
                .username(user.getUsername().toLowerCase())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .build());
        return a;
    }
}