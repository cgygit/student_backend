package com.example.demo.vo;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class ClassBarResult {
    //响应码
    private int code;
    // 数据 json格式
    private List<Integer> attendance;
    private List<Integer> point;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Integer> getAttendance() {
        return attendance;
    }

    public void setAttendance(List<Integer> attendance) {
        this.attendance = attendance;
    }

    public List<Integer> getPoint() {
        return point;
    }

    public void setPoint(List<Integer> point) {
        this.point = point;
    }
}
