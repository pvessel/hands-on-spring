package com.handsonspring.service.grid;

import com.handsonspring.model.Role;
import com.handsonspring.model.User;
import com.handsonspring.model.grid.GridEntries;
import com.handsonspring.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserGridColumnsServiceImplTest {

    @Mock
    private UserServiceImpl userService;

    private UserGridColumnsServiceImpl userGridService;

    @BeforeEach
    void initUseCase() {
        userGridService = new UserGridColumnsServiceImpl(userService);
    }

    @Test
    void getGridValues_whenEntityProvided_thenGridValuesReturned() {
        // given
        User user = new User();
        user.setRole(Role.ADMIN);
        String password = "password";
        user.setPassword(password);
        String email = "user@email.com";
        user.setEmail(email);
        UUID id = UUID.randomUUID();
        user.setId(id);
        String username = "John";
        user.setUsername(username);
        List<User> users = Collections.singletonList( user );

        // when
        List<Object> columnValues = userGridService.getGridValues(users);

        // then
        List<Object> oneRowValues = (List<Object>) columnValues.get(0);
        assertEquals(id, oneRowValues.get(0));
        assertEquals(username, oneRowValues.get(1));
        assertEquals(email, oneRowValues.get(2));
        assertEquals("", oneRowValues.get(3));
        assertEquals(Role.ADMIN.getName(), oneRowValues.get(4));
    }

    @Test
    void saveEntities_whenEntriesProvided_entitySaved() {
        // given
        UUID id = UUID.randomUUID();
        Object[] columns = {"", "username", "email@email.com", "password", "Admin"};
        List<Object[]> data = Collections.singletonList(columns);
        GridEntries gridEntries = mock(GridEntries.class);
        when(gridEntries.getData()).thenReturn(data);

        // when
        userGridService.saveEntities(gridEntries);

        // then
        Mockito.verify(userService, times(1)).save(any());
    }
}