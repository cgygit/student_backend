package com.example.demo.vo;

import com.alibaba.fastjson.JSONObject;

public class ChartResult {
    //响应码
    private int code;
    // 数据 json格式
    private JSONObject data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
