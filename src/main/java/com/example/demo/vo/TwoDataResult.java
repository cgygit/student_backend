package com.example.demo.vo;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class TwoDataResult {
    //响应码
    private int code;
    // 数据 json格式
    private List<Integer> data1;
    private List<Integer> data2;
    // x轴
    private List<String> xAxis;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Integer> getData1() {
        return data1;
    }

    public void setData1(List<Integer> data1) {
        this.data1 = data1;
    }

    public List<Integer> getData2() {
        return data2;
    }

    public void setData2(List<Integer> data2) {
        this.data2 = data2;
    }

    public List<String> getxAxis() {
        return xAxis;
    }

    public void setxAxis(List<String> xAxis) {
        this.xAxis = xAxis;
    }
}
