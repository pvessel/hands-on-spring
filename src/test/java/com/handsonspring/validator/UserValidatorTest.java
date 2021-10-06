package com.handsonspring.validator;

import com.handsonspring.service.UserService;
import com.handsonspring.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserService userService;

    private UserValidator userValidator;

    @BeforeEach
    void initUseCase() {
        userValidator = new UserValidator(userService);
    }

    @ParameterizedTest(name = "{index} => username={0}, password={1}, passswordConfirm={2}, usernameInUse={3}, expectedNumberOfErrors={4}")
    @CsvSource({
            "username, password, password, false, 0",
            "short, password, password, false, 1",
            "username, short, short, false, 1",
            "username, password, differentPassword, false, 1",
            "username, password, password, true, 1",
    })
    void validate(String username, String password, String passswordConfirm, String usernameInUseAsString, String expectedNumberOfErrorsAsString) {
        // given
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirm(passswordConfirm);

        Boolean usernameInUse = Boolean.valueOf(usernameInUseAsString);
        if(usernameInUse) {
            when(userService.findByUsername(username)).thenReturn(user);
        }

        Errors errors = mock(Errors.class);
        Integer expectedNumberOfErrors = Integer.valueOf(expectedNumberOfErrorsAsString);

        // when
        userValidator.validate(user, errors);

        // then
        Mockito.verify(errors, times(expectedNumberOfErrors)).rejectValue(any(), any());
    }
}