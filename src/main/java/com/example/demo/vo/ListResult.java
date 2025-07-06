package com.example.demo.vo;

import java.util.List;

public class ListResult {
    private List<Course> list;

    private  int total;

    public ListResult(List<Course> list, int total) {
        this.list = list;
        this.total = total;
    }

    public List<Course> getList() {
        return list;
    }

    public void setList(List<Course> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
