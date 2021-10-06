package com.handsonspring.service;

import com.handsonspring.model.Role;
import com.handsonspring.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void save(User user);

    User findByUsername(String username);

    User findById(UUID id);

    List<User> findAll();

    List<User> findAllWithRoles(List<Role> roles);

    User findByEmail(String email);

    void updatePassword(String updatedPassword, UUID id);
}
