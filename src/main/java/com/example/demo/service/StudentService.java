package com.example.demo.service;

import com.example.demo.vo.StudentResult;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    public StudentResult getStudentList(String stuID, int pagenum, int pagesize) {
        StudentResult studentResult = new StudentResult();
        return studentResult;
    }
}
