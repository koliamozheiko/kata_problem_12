package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminRestController {

    private final UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> showAllUsers() {
        return new ResponseEntity<>(userService.allUsers(), HttpStatus.OK);
    }

    @GetMapping("/user/root")
    public ResponseEntity<User> authUser(Authentication authentication)  {
        return new ResponseEntity<>(userService.findByUsername(authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public User getAdminPage(@PathVariable("id") Long id) {
        return userService.findUserById(id);
    }

    @PostMapping("/user")
    public User saveNewUser(@RequestBody User user) {
        userService.saveDefaultUser(user);
        return user;
    }

    @PutMapping("/user")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return String.format("User with id %d deleted", id);
    }
}
