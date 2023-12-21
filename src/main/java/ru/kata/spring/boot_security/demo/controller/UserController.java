package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public String showUserPage(@PathVariable("id") Long id, Authentication authentication) {
        if (!id.equals(userService.findByUsername(authentication.getName()).getId())
                && !userService.findByUsername(authentication.getName()).getRoles().toString().contains("ROLE_ADMIN")) {
            return "acces-denied";
        }
        return "user";
    }
}
