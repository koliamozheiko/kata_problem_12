package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService  {
    User findByUsername(String username);
    UserDetails loadUserByUsername(String username);
    User findUserById(Long id);
    List<User> allUsers();
    boolean saveDefaultUser(User user);
    boolean deleteUserById(Long id);
    boolean updateUser(User user);
}
