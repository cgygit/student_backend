package com.example.demo.vo;

import java.util.List;

public class OneDataResult {
    //响应码
    private int code;
    // 数据 json格式
    private List<Integer> data;
    // x轴
    private List<String> xAxis;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public List<String> getxAxis() {
        return xAxis;
    }

    public void setxAxis(List<String> xAxis) {
        this.xAxis = xAxis;
    }
}
