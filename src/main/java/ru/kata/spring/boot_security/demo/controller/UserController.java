package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class UserController {

    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String showUserPage(@RequestParam("id") Long id, Authentication authentication, Model model) {
        if (!id.equals(userService.findByUsername(authentication.getName()).getId())
                && !userService.findByUsername(authentication.getName()).getRoles().toString().contains("ROLE_ADMIN")) {
            return "acces-denied";
        }
        model.addAttribute("userPage", userService.findUserById(id));
        return "user";
    }
}
