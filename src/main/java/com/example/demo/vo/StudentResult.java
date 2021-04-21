package com.example.demo.vo;

import java.util.List;
import java.util.Map;

public class StudentResult {
    //响应码
    private int code;
    //学生信息
    //private List<Student> students;
    private List<Map<String,String>> students;
    //学生个数
    private int total;
    //列名
    //private List<String> columns;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

//    public List<Student> getStudents() {
////        return students;
////    }
////
////    public void setStudents(List<Student> students) {
////        this.students = students;
////    }


    public List<Map<String, String>> getStudents() {
        return students;
    }

    public void setStudents(List<Map<String, String>> students) {
        this.students = students;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

//    public List<String> getColumns() {
//        return columns;
//    }
//
//    public void setColumns(List<String> columns) {
//        this.columns = columns;
//    }
}
