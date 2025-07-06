package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.vo.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        User requestUser = new User();
        requestUser.setUsername("testuser");
        requestUser.setPassword("password");

        User foundUser = new User();
        foundUser.setUsername("testuser");
        foundUser.setPassword("password");

        when(userDao.findByUsernameAndPassword("testuser", "password")).thenReturn(foundUser);

        Result result = loginService.login(requestUser);

        assertEquals(200, result.getCode());
    }

    @Test
    void testLogin_Failure() {
        User requestUser = new User();
        requestUser.setUsername("testuser");
        requestUser.setPassword("wrongpassword");

        when(userDao.findByUsernameAndPassword("testuser", "wrongpassword")).thenReturn(null);

        Result result = loginService.login(requestUser);

        assertEquals(400, result.getCode());
    }
}
