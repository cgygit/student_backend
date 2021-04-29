package com.example.demo.dao;

import com.example.demo.entity.Course;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.charset.CoderResult;

public interface CourseDao extends JpaRepository<Course,Integer> {
    Course save(Course course);

    //Course findFirstByCourseIdOrderByUpdateAtDesc(int id);

    Course findByName(String name);
}
