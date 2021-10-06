package com.handsonspring.service;

import com.handsonspring.model.Role;
import com.handsonspring.model.User;
import com.handsonspring.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    void initUseCase() {
        userService = new UserServiceImpl(userRepository, bCryptPasswordEncoder);
    }

    @Test
    void save_whenUserWithoutPasswordAndRole_thenDonNotModifyPasswordAndSetUpRole() {
        // given
        User user = new User();

        // when
        userService.save(user);

        // then
        assertEquals(user.getPassword(), null);
        assertEquals(user.getRoleAsString(), "Admin");
        Assertions.assertEquals(user.getRole(), Role.ADMIN);
    }

    @Test
    void save_whenUserWithPasswordAndRole_thenModifyPasswordAndDoNotModifyRole() {
        // given
        User user = new User();
        String password = "topSecretPassword";
        user.setPassword(password);
        when(bCryptPasswordEncoder.encode(password)).thenReturn("encodedTopSecretPassword");
        user.setRole(Role.USER);

        // when
        userService.save(user);

        // then
        assertEquals(user.getPassword(), "encodedTopSecretPassword");
        assertEquals(user.getRoleAsString(), "User");
        assertEquals(user.getRole(), Role.USER);
    }

    @Test
    void findByUsername_whenCalled_thenUserFromRepositoryReturned() {
        // given
        User user = new User();
        String username = "John";
        when(userRepository.findByUsername(username)).thenReturn(user);

        // when
        User userFound = userService.findByUsername(username);

        // then
        assertEquals(userFound, user);
    }

    @Test
    void findById_whenCalled_thenUserFromRepositoryReturned() {
        // given
        User user = new User();
        Optional<User> userOptional = Optional.of(user);
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(userOptional);

        // when
        User userFound = userService.findById(userId);

        // then
        assertEquals(userFound, user);
    }

    @Test
    void findAll_whenCalled_thenUsersFromRepositoryReturned() {
        // given
        User user = new User();
        List<User> users = Collections.singletonList( user );
        when(userRepository.findAll()).thenReturn(users);

        // when
        List<User> usersFound = userService.findAll();

        // then
        assertEquals(usersFound, users);
    }

    @Test
    void findAllWithRoles_whenCalled_thenUsersFromRepositoryReturned() {
        // given
        User user = new User();
        Role role = Role.ADMIN;
        List<User> users = Collections.singletonList( user );
        List<Role> roles = Collections.singletonList( role );
        when(userRepository.findByRoleIn(roles)).thenReturn(users);

        // when
        List<User> usersFound = userService.findAllWithRoles(roles);

        // then
        assertEquals(usersFound, users);
    }

    @Test
    void findByEmail_whenCalled_thenUserFromRepositoryReturned() {
        // given
        User user = new User();
        String email = "john@mail.com";
        when(userRepository.findByEmail(email)).thenReturn(user);

        // when
        User userFound = userService.findByEmail(email);

        // then
        assertEquals(userFound, user);
    }

    @Test
    void updatePassword_whenCalled_thenUserUpdate() {
        // given
        String password = "thisIsVerySecretPassword";
        UUID userId = UUID.randomUUID();

        // when
        userService.updatePassword(password, userId);

        // then
        Mockito.verify(userRepository, times(1)).updatePassword(password, userId );
    }
}