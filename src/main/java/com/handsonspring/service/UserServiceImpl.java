package com.handsonspring.service;

import com.handsonspring.repository.UserRepository;
import com.handsonspring.model.Role;
import com.handsonspring.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void save(User user) {
        if(null != user.getPassword() && !user.getPassword().isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if(null == user.getRole() || user.getRoleAsString().isEmpty()) {
            user.setRole(Role.ADMIN);
        }
        user.setPasswordConfirm("");

        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllWithRoles(List<Role> roles) {
        return userRepository.findByRoleIn(roles);
    }

    @Override
    public User findByEmail(String email)  {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updatePassword(String updatedPassword, UUID id) {
        userRepository.updatePassword(updatedPassword, id);
    }
}
