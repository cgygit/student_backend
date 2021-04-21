package com.example.demo.utils;


import com.example.demo.vo.SheetContent;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelImportUtil {
    private Workbook wb;
    private Sheet sheet;
    private Row row;

    // 读取excel
    public ExcelImportUtil(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        String ext = filename.substring(filename.lastIndexOf("."));
        InputStream is = file.getInputStream();
        if (".xls".equals(ext)) {
            wb = new HSSFWorkbook(is);
        } else if (".xlsx".equals(ext)) {
            wb = new XSSFWorkbook(is);
        } else {
            wb = null;
        }
    }

    // 读取所有sub sheet内容  分成三类
    public List<List<SheetContent>> readExcelSubContent() {
        List<List<SheetContent>> list = new ArrayList<List<SheetContent>>();
        List<List<Integer>> sheetIndexList = readSheetIndex();
        List<SheetContent> list0 = readExcelSubContentByType(sheetIndexList.get(0), 3);
        List<SheetContent> list1 = readExcelSubContentByType(sheetIndexList.get(1), 2);
        List<SheetContent> list2 = readExcelSubContentByType(sheetIndexList.get(2), 1);
        list.add(list0);
        list.add(list1);
        list.add(list2);
        return list;
    }

    // 读取多个sub sheet内容  传入sheet号列表
    public List<SheetContent> readExcelSubContentByType(List<Integer> sheetIndex, int rowIndex) {
        List<SheetContent> allList = new ArrayList<SheetContent>();
        // 应该是遍历sheetIndex才对！！！！！！不是wb.getSheetAt(i)
        for (int i = 0; i < sheetIndex.size(); i ++) {
            List<Map<String, String>> list = new ArrayList<>();
            sheet = wb.getSheetAt(sheetIndex.get(i));
            int rowNum = sheet.getLastRowNum();
            int realRowIndex;
            // 因为课堂活动 合并单元格 读的是第三行而非第四行
            if(rowIndex == 3) {
                realRowIndex = 2;
            }
            else {
                realRowIndex = rowIndex;
            }
            String[] headers = readExcelSubHeader(sheetIndex.get(i), realRowIndex);
            // 正文在rowIndex的下一行
            for (int j = rowIndex+1; j <= rowNum; j ++) {
                row = sheet.getRow(j);
                Map<String, String> map = new LinkedHashMap<>();
                for (int k = 0; k < headers.length; k ++) {
                    String str = parseExcel(row.getCell(k));    // 获得单元格内容
                    map.put(headers[k], str);
                }
                list.add(map);
            }
            SheetContent subContent = new SheetContent();
            subContent.setHeaders(headers);
            subContent.setSheetContent(list);
            subContent.setSheetName(wb.getSheetName(sheetIndex.get(i)));
            allList.add(subContent);
        }
        return allList;
    }

    // 获得三种类型的sheet号
    public List<List<Integer>> readSheetIndex() {
        List<List<Integer>> list = new ArrayList<List<Integer>>();
        List<Integer> lesson = new ArrayList<Integer>();
        List<Integer> courseware = new ArrayList<Integer>();
        List<Integer> test = new ArrayList<Integer>();
        if (wb != null) {
            int sheetNum = wb.getNumberOfSheets();
            for (int i = 0; i < sheetNum; i ++) {
                String sheetName = wb.getSheetName(i);
                if (sheetName.indexOf("课堂情况") != -1) {
                    lesson.add(i);
                }
                else if(sheetName.indexOf("课件推送") != -1) {
                    courseware.add(i);
                }
                else if(sheetName.indexOf("试卷") != -1) {
                    test.add(i);
                }
            }
            list.add(lesson);
            list.add(courseware);
            list.add(test);
        }
        return list;
    }


    // 读取sub sheet表头  有三种类型  传入sheet和row
    public String[] readExcelSubHeader(int sheetIndex, int rowIndex) {
        String[] headers = {};
        if (wb != null) {
            sheet = wb.getSheetAt(sheetIndex);
            row = sheet.getRow(rowIndex);
            int colNum = row.getPhysicalNumberOfCells();
            headers = new String[colNum];
            for (int i = 0; i < colNum; i++) {
                headers[i] = parseExcel(row.getCell(i));
                headers[i] = headers[i].replace(" (", "_");
                headers[i] = headers[i].replace("(", "_");
                headers[i] = headers[i].replace("（", "_");
                headers[i] = headers[i].replace(")", "");
                headers[i] = headers[i].replace("）", "");
                headers[i] = headers[i].replace(".0", "");
                headers[i] = headers[i].replace(":", "");
                if ( headers[i].contains("题")) {
                    int x = headers[i].indexOf("题");
                    headers[i] = headers[i].substring(0, x+1);    // “题”后面的都不要了
                }
            }
        }
        return headers;
    }



    // 读取sheet0内容
    public SheetContent readExcelSumContent() {
        List<Map<String, String>> list = new ArrayList<>();
        SheetContent sumContent = new SheetContent();
        if (wb != null) {
            sheet = wb.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();
            String[] headers = readExcelSumHeader();
            sumContent.setHeaders(headers);
            for (int i = 2; i <= rowNum; i++) {              // 从第三行开始是数据
                row = sheet.getRow(i);
                Map<String, String> map = new LinkedHashMap<>();
                for (int j = 0; j < 8; j++) {
                    String str = parseExcel(row.getCell(j));
                    map.put(headers[j], str);        // 每个表格的数据 键值对
                }
                if (!map.isEmpty()) {
                    list.add(map);      // 存入一行数据
                }
            }
        }
        sumContent.setSheetContent(list);
        sumContent.setSheetName(wb.getSheetName(0));
        return sumContent;
    }


    // 读取sheet0表头  因为只读前八列所以单独成方法
    public String[] readExcelSumHeader() {
        String[] headers = {};
        if (wb != null) {
            sheet = wb.getSheetAt(0);
            row = sheet.getRow(1);
            headers = new String[8];        // 只获取汇总数据
            for (int i = 0; i < 8; i++) {
                headers[i] = parseExcel(row.getCell(i));
                // 加中括号转义不行 只能每种都规范
                headers[i] = headers[i].replace("(", "_");
                headers[i] = headers[i].replace("（", "_");
                headers[i] = headers[i].replace(")", "");
                headers[i] = headers[i].replace("）", "");
                headers[i] = headers[i].replace(".0", "");

//                if (headers[i].contains(" ") || headers[i].contains("(") || headers[i].contains("（") || headers[i].contains(".") || headers[i].contains(":")) {
//                    headers[i] = "[" + headers[i] + "]";
//                }
            }
        }
        return headers;
    }



    // 根据Cell类型设置数据
    private String parseExcel(Cell cell) {
        String result = "";
        if (cell != null) {
            SimpleDateFormat sdf = null;
            int cellType = cell.getCellType();
            switch (cellType) {
                // 字符串类型
                case Cell.CELL_TYPE_STRING:
                    result = StringUtils.isEmpty(cell.getStringCellValue()) ? "" : cell.getStringCellValue().trim();
                    result = result.replaceAll("\\\\", "\\\\\\\\");
                    break;
                // 布尔类型
                case Cell.CELL_TYPE_BOOLEAN:
                    result = String.valueOf(cell.getBooleanCellValue());
                    break;
                // 数值类型
                case Cell.CELL_TYPE_NUMERIC:
                    result = new DecimalFormat("#.######").format(cell.getNumericCellValue());
                    break;
                // 取空串
                default:
                    break;
            }
        }
        return result;
    }


}
