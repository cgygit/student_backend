package com.example.demo.vo;

import java.util.List;
import java.util.Map;

public class StatListResult {
    private int code;
    private int total;
    private List<Map<String,String>> students;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Map<String, String>> getStudents() {
        return students;
    }

    public void setStudents(List<Map<String, String>> students) {
        this.students = students;
    }
}
