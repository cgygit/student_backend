package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.pojo.User;
import com.example.demo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginService<UserDAO> {
    @Autowired
    UserDao userDao;

    // 登录
    public Result login(User requestUser) {
        String username = requestUser.getUsername();
        String password = requestUser.getPassword();
        User user = userDao.getByUsernameAndPassword(username,password);

        if (null == user) {
            return new Result(400);
        } else {
            return new Result(200);
        }
    }
}
