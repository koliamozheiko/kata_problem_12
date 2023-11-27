package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
public class AdminController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String userList(Model model, Authentication authentication) {
        if (userService.findByUsername(authentication.getName()) != null) {
            if (!userService.findByUsername(authentication.getName()).getRoles().toString().contains("ROLE_ADMIN")) {
                return "redirect:user?id=" + userService.findByUsername(authentication.getName()).getId();
            }
        }
        model.addAttribute("allUsers", userService.allUsers());
        return "admin";
    }

    @PostMapping("/admin/remove")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        model.addAttribute("userForEdit", userService.findUserById(id));
        //model.addAttribute("allRoles", userService.showAllRoles());
        return "edit";
    }

    @PostMapping("/admin/edit")
    public String updateUser(@ModelAttribute("userForEdit") User user) {
       if (!userService.updateUser(user)) {
           return "edit";
       }
        return "redirect:/admin";
    }

    @GetMapping("/admin/new")
    public String newUser(Model model) {
        model.addAttribute("newUser", new User());
        return "new";
    }

    @PostMapping("/admin/new")
    public String createNewUser(@ModelAttribute("newUser") User user) {
        if(!user.getPassword().equals(user.getPasswordConfirm())) {
            return "new";
        }

        if(userService.saveDefaultUser(user)) {
            return "new";
        }
        return "redirect:/admin";
    }
}
