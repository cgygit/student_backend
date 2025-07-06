package com.example.demo.vo;

import java.util.List;
import java.util.Map;

public class SheetContent {
    private List<Map<String, String>> sheetContent;
    private String sheetName;
    private String[] headers;

    public List<Map<String, String>> getSheetContent() {
        return sheetContent;
    }

    public void setSheetContent(List<Map<String, String>> sheetContent) {
        this.sheetContent = sheetContent;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }
}
