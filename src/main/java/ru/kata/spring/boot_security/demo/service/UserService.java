package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private EntityManager entityManager;
    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null ) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return user;
    }

    public User findUserById(Long id) {
        Optional<User> userFromDb = userRepository.findById(id);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }
    @Transactional
    public boolean saveDefaultUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return true;
        }

        user.addRole(roleRepository.findByName("ROLE_USER"));
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return false;
    }

    public boolean deleteUserById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateUser(User user) {

        if (!user.getPassword().startsWith("$2a$10$")) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        entityManager.merge(user);
        return true;

    }

    public List<Role> showAllRoles() {
        return roleRepository.findAll();
    }

}
