package com.example.demo.utils;


import com.example.demo.vo.ScoreResult;
import com.example.demo.vo.SheetContent;
import com.example.demo.vo.Student;
import com.example.demo.vo.StudentResult;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import java.sql.*;
import java.util.*;

public class SqlUtil {
    // 对应数据库配置 在application.properties
    private String driver = "com.mysql.jdbc.Driver";

    private String url = "jdbc:mysql://127.0.0.1:3306/project?characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private String userName = "root";

    private String password = "123456";

    // 创建数据库表 并 插入数据
    public void initTable(List<String> tableNameList, List<String[]> headersList, List<List<Map<String, String>>> tableContentList) throws SQLException, ClassNotFoundException {
        //连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();
        // tableNameList与headersList长度应相等
        for (int j = 0; j < tableNameList.size(); j ++) {
            String tableName = tableNameList.get(j);
            String[] headers = headersList.get(j);
            List<Map<String, String>> tableContent = tableContentList.get(j);
            ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null);
            // 判断表是否存在，如果存在则什么都不做，否则创建表
            if (rs.next()) {
                continue;
            } else {
                // 创建table
                int num = headers.length;
                String sqlCommand = "CREATE TABLE " + tableName + " (" + "ID INT AUTO_INCREMENT,";
                for (int i = 0; i < num; i++) {
                    sqlCommand = sqlCommand + headers[i] + " varchar(100),";
                }
                sqlCommand = sqlCommand + "PRIMARY KEY (ID)"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
                stat.executeUpdate(sqlCommand);

                // 插入数据
                int rowNum = tableContent.size();
                for (int k = 0; k < rowNum; k ++) {
                    Map<String, String> map = tableContent.get(k);
                    String insertCommand = "INSERT INTO " + tableName;
                    String fields = "(";
                    String values = "(";
                    for (String key : map.keySet()) {
                        fields = fields + key + ",";
                        String value = map.get(key);
                        values = values + "\"" + value + "\",";
                    }
                    fields = fields.substring(0, fields.length()-1) + ")";
                    values = values.substring(0, values.length()-1) + ")";
                    insertCommand = insertCommand + fields + "VALUES" + values + ";";
                    System.out.println(insertCommand);
                    stat.executeUpdate(insertCommand);
                }

            }
        }
        // 释放资源
        stat.close();
        conn.close();
    }

    public void updateUser(String course, String username) throws ClassNotFoundException, SQLException {
        //连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        String sql1 = "select course from user where username = '" + username + "';";
        ResultSet rs = stat.executeQuery(sql1);
        // username是唯一的
        if (rs.next()) {
            String oldCourse = rs.getString("course");
            String newCourse = oldCourse + course + ",";
            String sql2 = "update user set course = '" + newCourse + "' where username = '" + username + "';";
            stat.executeUpdate(sql2);
        }

        // 释放资源
        stat.close();
        conn.close();
    }

    // 删除数据库中以fileName为前缀的表
    public boolean removeTables(String name) throws SQLException, ClassNotFoundException {
        boolean flag = true;
        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        String sqlCon = "select table_name from information_schema.tables where table_schema='project' and table_name regexp '^" + name +"';";
        ResultSet rs = stat.executeQuery(sqlCon);   // 获得查询结果
        //  while(rs.next())遍历会报错Operation not allowed after ResultSet closed  因此将结果转成result类型
        Result result = ResultSupport.toResult(rs);
        if(result!=null && result.getRowCount()!=0){
            for(int i=0 ; i<result.getRowCount(); i++){
                Map row = result.getRows()[i];
                String tableName = row.get("TABLE_NAME").toString();
                String sqlDrop = "drop table " + tableName + ";";
                boolean f = stat.execute(sqlDrop);
                if (f == false) {
                    flag = false;
                }
            }
        }

        // 释放资源
        stat.close();
        conn.close();
        return flag;
    }

    public void updateByReplace(String course, List<String> tableNameList, List<String[]> headersList, List<List<Map<String, String>>> tableContentList) throws ClassNotFoundException, SQLException {
        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        String sql1 = "drop table " + course + "_" + course + "_数据汇总;";
        stat.executeUpdate(sql1);

        for (int j = 0; j < tableNameList.size(); j ++) {
            String tableName = tableNameList.get(j);
            String[] headers = headersList.get(j);
            List<Map<String, String>> tableContent = tableContentList.get(j);
            ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null);
            // 判断表是否存在，如果存在则什么都不做，否则创建表
            if (rs.next()) {
                continue;
            } else {
                // 创建table
                int num = headers.length;
                String sqlCommand = "CREATE TABLE " + tableName + " (" + "ID INT AUTO_INCREMENT,";
                for (int i = 0; i < num; i++) {
                    sqlCommand = sqlCommand + headers[i] + " varchar(100),";
                }
                sqlCommand = sqlCommand + "PRIMARY KEY (ID)"
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
                stat.executeUpdate(sqlCommand);

                // 插入数据
                int rowNum = tableContent.size();
                for (int k = 0; k < rowNum; k ++) {
                    Map<String, String> map = tableContent.get(k);
                    String insertCommand = "INSERT INTO " + tableName;
                    String fields = "(";
                    String values = "(";
                    for (String key : map.keySet()) {
                        fields = fields + key + ",";
                        String value = map.get(key);
                        values = values + "\"" + value + "\",";
                    }
                    fields = fields.substring(0, fields.length()-1) + ")";
                    values = values.substring(0, values.length()-1) + ")";
                    insertCommand = insertCommand + fields + "VALUES" + values + ";";
                    stat.executeUpdate(insertCommand);
                }
            }
        }

        // 释放资源
        stat.close();
        conn.close();
    }

    public void updateByAdd(String course, List<String> tableNameList, List<String[]> headersList, List<List<Map<String, String>>> tableContentList) throws ClassNotFoundException, SQLException {
        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        // todo

        // 释放资源
        stat.close();
        conn.close();
    }

    public StudentResult getStudentList(String courseName) throws ClassNotFoundException, SQLException {
        StudentResult studentResult = new StudentResult();
        studentResult.setCode(400);
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        String sql = "select * from " + courseName + "_" + courseName + "_数据汇总;";
        ResultSet rs = stat.executeQuery(sql);
        Result result = ResultSupport.toResult(rs);
        String[] colName = result.getColumnNames();

        if(result!=null && result.getRowCount()!=0){
            studentResult.setCode(200);
            for(int i=0 ; i<result.getRowCount(); i++){
                Map<String,String> student = new LinkedHashMap<String,String>();
                Map row = result.getRows()[i];
                String stuId = row.get(colName[1]).toString();
                student.put(colName[1], stuId);
                String score = row.get(colName[2]).toString();
                student.put(colName[2], score);
                String page = row.get(colName[3]).toString();
                student.put(colName[3], page);
                String attendance = row.get(colName[4]).toString();
                student.put(colName[4], attendance);
                String barrage = row.get(colName[6]).toString();
                student.put(colName[6], barrage);
                String submission = row.get(colName[7]).toString();
                student.put(colName[7], submission);
                String announcement = row.get(colName[8]).toString();
                student.put(colName[8], announcement);

                list.add(student);

//                Student student = new Student();
//                Map row = result.getRows()[i];
//                String stuId = row.get(colName[1]).toString();
//                int score = Integer.parseInt(row.get(colName[2]).toString());
//                int page = Integer.parseInt(row.get(colName[3]).toString());
//                int attendance = Integer.parseInt(row.get(colName[4]).toString());
//                int barrage = Integer.parseInt(row.get(colName[6]).toString());
//                int submission = Integer.parseInt(row.get(colName[7]).toString());
//                int announcement = Integer.parseInt(row.get(colName[8]).toString());
//                student.setStuId(stuId);
//                student.setScore(score);
//                student.setPage(page);
//                student.setAttendance(attendance);
//                student.setBarrage(barrage);
//                student.setSubmission(submission);
//                student.setAnnouncement(announcement);
//                students.add(student);

//                student.setCol1(colName[1]);
//                student.setCol2(colName[2]);
//                student.setCol3(colName[3]);
//                student.setCol4(colName[4]);
//                student.setCol6(colName[6]);
//                student.setCol7(colName[7]);
//                student.setCol8(colName[8]);
            }
            studentResult.setStudents(list);
            studentResult.setTotal(result.getRowCount());
        }
        // 释放资源
        stat.close();
        conn.close();

        return studentResult;
    }

    public StudentResult getStudentByID(String courseName, String stuID) throws ClassNotFoundException, SQLException {
        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        StudentResult studentResult = new StudentResult();
        String sql = "select * from " + courseName + "_" + courseName + "_数据汇总 where 学号='" + stuID + "';";
        ResultSet rs = stat.executeQuery(sql);
        Result result = ResultSupport.toResult(rs);
        Map row = result.getRows()[0];  // 学号不重复 只有一行
        String[] colName = result.getColumnNames();

        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Map<String,String> student = new LinkedHashMap<String,String>();
        String stuId = row.get(colName[1]).toString();
        student.put(colName[1], stuId);
        String score = row.get(colName[2]).toString();
        student.put(colName[2], score);
        String page = row.get(colName[3]).toString();
        student.put(colName[3], page);
        String attendance = row.get(colName[4]).toString();
        student.put(colName[4], attendance);
        String barrage = row.get(colName[6]).toString();
        student.put(colName[6], barrage);
        String submission = row.get(colName[7]).toString();
        student.put(colName[7], submission);
        String announcement = row.get(colName[8]).toString();
        student.put(colName[8], announcement);
        list.add(student);

        studentResult.setStudents(list);
        studentResult.setTotal(result.getRowCount());
        studentResult.setCode(200);

        // 释放资源
        stat.close();
        conn.close();
        return studentResult;
    }

    public int getNumberFromStr(String str) {
        int number = 0;
        List<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < str.length(); i ++) {
            char x = str.charAt(i);
            if(x >= 48 && x <= 57) {
                numbers.add(x-'0');
            }
        }

        int k = 1;
        for (int i = numbers.size()-1; i >= 0; i --) {
            number += numbers.get(i)*k;
            k *= 10;
        }
        return number;
    }

    public float getScore(String term, int total, Object weight) {
        float score = 0;
        if (total != 0) {
            score = (Float.parseFloat(term)/total)*100*Float.parseFloat(weight.toString());
        }
        // 如果总分为0 如通告数 则记满分
        else {
            score = 100*Float.parseFloat(weight.toString());
        }
        return score;
    }

    public ScoreResult getScoreList(String courseName, Map<Object,Object> setting) throws ClassNotFoundException, SQLException {
        ScoreResult scoreResult = new ScoreResult();
        scoreResult.setCode(400);
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();

        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();


        String sql = "select * from " + courseName + "_" + courseName + "_数据汇总;";
        ResultSet rs = stat.executeQuery(sql);
        Result result = ResultSupport.toResult(rs);
        String[] colName = result.getColumnNames();

        // 获得总分等信息
        int totalPoint = getNumberFromStr(colName[2]);
        int totalPage = getNumberFromStr(colName[3]);
        int totalAttendance = getNumberFromStr(colName[4]);
        int totalAnnouncement = getNumberFromStr(colName[8]);


        if(result!=null && result.getRowCount()!=0){
            scoreResult.setCode(200);
            for(int i=0 ; i<result.getRowCount(); i++){
                Map row = result.getRows()[i];
                Map<String,String> student = new LinkedHashMap<String,String>();
                String stuId = row.get(colName[1]).toString();
                student.put(colName[1], stuId);
                String point = row.get(colName[2]).toString();
                student.put(colName[2], point);
                String page = row.get(colName[3]).toString();
                student.put(colName[3], page);
                String attendance = row.get(colName[4]).toString();
                student.put(colName[4], attendance);
                String barrage = row.get(colName[6]).toString();
                student.put(colName[6], barrage);
                String submission = row.get(colName[7]).toString();
                student.put(colName[7], submission);
                String announcement = row.get(colName[8]).toString();
                student.put(colName[8], announcement);


                // 计算平均成绩
                float average = getScore(point, totalPoint, setting.get("point"))
                        + getScore(page, totalPage, setting.get("page"))
                        + getScore(attendance, totalAttendance, setting.get("attendance"))
                        + getScore(announcement, totalAnnouncement, setting.get("announcement"));


                // 弹幕和投稿没有总分 用其他方式算
                int ba = Integer.parseInt(barrage);
                int su = Integer.parseInt(submission);
                if(ba <= 10) {
                    average += ba*10*Float.parseFloat(setting.get("barrage").toString());
                }
                else if(ba > 10) {
                    average += 100*Float.parseFloat(setting.get("barrage").toString());
                }
                if(su <= 10) {
                    average += su*10*Float.parseFloat(setting.get("submission").toString());
                }
                else if(su > 10) {
                    average += 100*Float.parseFloat(setting.get("submission").toString());
                }

                student.put("平时成绩", String.format("%.2f", average));

                list.add(student);
            }
            scoreResult.setScores(list);
            scoreResult.setTotal(result.getRowCount());
        }
        // 释放资源
        stat.close();
        conn.close();

        return scoreResult;
    }

    public int getTotalAttendance(String courseName) throws ClassNotFoundException, SQLException {
        String[] colName = getColName(courseName);
        int totalAttendance = getNumberFromStr(colName[4]);
        return totalAttendance;
    }

    public String[] getColName(String courseName) throws ClassNotFoundException, SQLException {
        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        String sql= "";
        sql = "select * from " + courseName + "_" + courseName + "_数据汇总;";

        ResultSet rs = stat.executeQuery(sql);
        Result result = ResultSupport.toResult(rs);
        String[] colName = result.getColumnNames();
        return colName;
    }

    public ScoreResult getScoreById(String courseName, Map<Object,Object> setting, String stuID) throws ClassNotFoundException, SQLException {
        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        ScoreResult scoreResult = new ScoreResult();
        String sql = "select * from " + courseName + "_" + courseName + "_数据汇总 where 学号='" + stuID + "';";
        ResultSet rs = stat.executeQuery(sql);
        Result result = ResultSupport.toResult(rs);
        Map row = result.getRows()[0];  // 学号不重复 只有一行
        String[] colName = result.getColumnNames();

        // 获得总分等信息
        int totalPoint = getNumberFromStr(colName[2]);
        int totalPage = getNumberFromStr(colName[3]);
        int totalAttendance = getNumberFromStr(colName[4]);
        int totalAnnouncement = getNumberFromStr(colName[8]);

        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Map<String,String> student = new LinkedHashMap<String,String>();
        String stuId = row.get(colName[1]).toString();
        student.put(colName[1], stuId);
        String point = row.get(colName[2]).toString();
        student.put(colName[2], point);
        String page = row.get(colName[3]).toString();
        student.put(colName[3], page);
        String attendance = row.get(colName[4]).toString();
        student.put(colName[4], attendance);
        String barrage = row.get(colName[6]).toString();
        student.put(colName[6], barrage);
        String submission = row.get(colName[7]).toString();
        student.put(colName[7], submission);
        String announcement = row.get(colName[8]).toString();
        student.put(colName[8], announcement);

        // 计算平均成绩
        float average = getScore(point, totalPoint, setting.get("point"))
                + getScore(page, totalPage, setting.get("page"))
                + getScore(attendance, totalAttendance, setting.get("attendance"))
                + getScore(announcement, totalAnnouncement, setting.get("announcement"));


        // 弹幕和投稿没有总分 用其他方式算
        int ba = Integer.parseInt(barrage);
        int su = Integer.parseInt(submission);
        if(ba <= 10) {
            average += ba*10*Float.parseFloat(setting.get("barrage").toString());
        }
        else if(ba > 10) {
            average += 100*Float.parseFloat(setting.get("barrage").toString());
        }
        if(su <= 10) {
            average += su*10*Float.parseFloat(setting.get("submission").toString());
        }
        else if(su > 10) {
            average += 100*Float.parseFloat(setting.get("submission").toString());
        }

        student.put("平时成绩", String.format("%.2f", average));

        list.add(student);

        scoreResult.setScores(list);
        scoreResult.setTotal(result.getRowCount());
        scoreResult.setCode(200);

        // 释放资源
        stat.close();
        conn.close();
        return scoreResult;
    }

}

