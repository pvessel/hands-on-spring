package com.handsonspring.service;

import com.handsonspring.model.Role;
import com.handsonspring.model.User;
import com.handsonspring.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    void initUseCase() {
        userDetailsServiceImpl = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsername() {
        // given
        String username = "John Smith";
        User user = new User();
        user.setRole(Role.ADMIN);
        user.setPassword("password");
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(user);

        // when
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        // then
        assertEquals(userDetails.getUsername(), "John Smith");
    }
}