package com.example.demo.dao;

import com.example.demo.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User,Integer> {
    User findByUsername(String username);

    User findByUsernameAndPassword(String username,String password);

    User save(User user);

//    @Modifying
//    @Query("update User set course = :course where username = :username")
//    int updateCourseByUsername(@Param("course")String course, @Param("username")String username);

}
