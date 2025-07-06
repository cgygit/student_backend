package com.example.demo.controller;

import com.example.demo.service.StudentService;
import com.example.demo.vo.Result;
import com.example.demo.vo.Student;
import com.example.demo.vo.StudentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
public class StudentController {
    @Autowired
    StudentService studentService;

    @CrossOrigin
    @GetMapping(value = "api/student")
    @ResponseBody
    public StudentResult getStudentList(@RequestParam("coursename")String courseName,
                                        @RequestParam("stuID")String stuID,
                                        @RequestParam("pagenum")int pagenum,
                                        @RequestParam("pagesize")int pagesize) throws SQLException, ClassNotFoundException {
        // 两种情况 获得全部list 和 获得制定stuID的
        return studentService.getStudentList(courseName, stuID, pagenum, pagesize);
    }
}
