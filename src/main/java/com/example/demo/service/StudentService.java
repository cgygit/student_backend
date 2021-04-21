package com.example.demo.service;

import com.example.demo.utils.SqlUtil;
import com.example.demo.vo.StudentResult;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class StudentService {
    public StudentResult getStudentList(String courseName, String stuID, int pagenum, int pagesize) throws SQLException, ClassNotFoundException {
        StudentResult studentResult = new StudentResult();
        SqlUtil sqlUtil = new SqlUtil();
        if(stuID.isEmpty()) {
            // stuID为空 获得所有。查汇总表
            studentResult = sqlUtil.getStudentList(courseName);
        }
        else {
            // 根据学号查
            studentResult = sqlUtil.getStudentByID(courseName, stuID);
        }
        return studentResult;
    }
}
