package com.example.demo.controller;

import com.example.demo.service.StudentService;
import com.example.demo.vo.Result;
import com.example.demo.vo.Student;
import com.example.demo.vo.StudentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentController {
    @Autowired
    StudentService studentService;

    @CrossOrigin
    @GetMapping(value = "api/student")
    @ResponseBody
    public StudentResult getStudentList(@RequestParam("stuID")String stuID,
                                        @RequestParam("pagenum")int pagenum,
                                        @RequestParam("pagesize")int pagesize) {
        return studentService.getStudentList(stuID, pagenum, pagesize);
    }
}
