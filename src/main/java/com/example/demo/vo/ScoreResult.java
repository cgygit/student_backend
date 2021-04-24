package com.example.demo.vo;

import java.util.List;
import java.util.Map;

public class ScoreResult {
    private int code;
    private List<Map<String,String>> scores;
    private int total;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Map<String, String>> getScores() {
        return scores;
    }

    public void setScores(List<Map<String, String>> scores) {
        this.scores = scores;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
