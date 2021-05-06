package com.example.demo.utils;


import com.example.demo.vo.*;
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

                // 数据统一化 解决异常情况
                if(tableName.contains("课堂情况")) {
                    if(!Arrays.asList(headers).contains("累计得分")) {
                        String sql = "alter table " + tableName + " add 累计得分 varchar(100) default'0' ;";
                        stat.executeUpdate(sql);
                    }
                }
                // 课件推送 跟 课堂情况 处理逻辑不一样。不填一列得分
//                if(tableName.contains("课件推送")) {
//                    if(headers.length <= 6) {
//                    }
//                }
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

    public void updateByAddScore(String course, List<List<String>> content) throws ClassNotFoundException, SQLException {
        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        // 新建一张成绩表
        String tableName = course + "_成绩";
        String sql1 = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "ID INT AUTO_INCREMENT," +
                "学号 VARCHAR(100) NOT NULL," +
                "作业成绩 VARCHAR(100) NOT NULL," +
                "期末成绩 VARCHAR(100) NOT NULL," +
                "PRIMARY KEY (ID) )ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        stat.executeUpdate(sql1);

        // 插入数据
        for(int i = 0; i < content.get(0).size(); i ++) {
            String sql2 = "INSERT INTO " + tableName + "(学号, 作业成绩, 期末成绩) values " +
                    "(" + content.get(0).get(i) + "," + content.get(1).get(i)  + "," + content.get(2).get(i) + ");";
            stat.executeUpdate(sql2);
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
                String name = row.get(colName[2]).toString();
                student.put(colName[2], name);
                String score = row.get(colName[3]).toString();
                student.put(colName[3], score);
                String page = row.get(colName[4]).toString();
                student.put(colName[4], page);
                String attendance = row.get(colName[5]).toString();
                student.put(colName[5], attendance);
                String barrage = row.get(colName[7]).toString();
                student.put(colName[7], barrage);
                String submission = row.get(colName[8]).toString();
                student.put(colName[8], submission);
                String announcement = row.get(colName[9]).toString();
                student.put(colName[9], announcement);

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
        String name = row.get(colName[2]).toString();
        student.put(colName[2], name);
        String score = row.get(colName[3]).toString();
        student.put(colName[3], score);
        String page = row.get(colName[4]).toString();
        student.put(colName[4], page);
        String attendance = row.get(colName[5]).toString();
        student.put(colName[5], attendance);
        String barrage = row.get(colName[7]).toString();
        student.put(colName[7], barrage);
        String submission = row.get(colName[8]).toString();
        student.put(colName[8], submission);
        String announcement = row.get(colName[9]).toString();
        student.put(colName[9], announcement);
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
        int totalPoint = getNumberFromStr(colName[3]);
        int totalPage = getNumberFromStr(colName[4]);
        int totalAttendance = getNumberFromStr(colName[5]);
        int totalAnnouncement = getNumberFromStr(colName[9]);


        if(result!=null && result.getRowCount()!=0){
            scoreResult.setCode(200);
            for(int i=0 ; i<result.getRowCount(); i++){
                Map row = result.getRows()[i];
                Map<String,String> student = new LinkedHashMap<String,String>();
                String stuId = row.get(colName[1]).toString();
                student.put(colName[1], stuId);
                String name = row.get(colName[2]).toString();
                student.put(colName[2], name);
                String point = row.get(colName[3]).toString();
                student.put(colName[3], point);
                String page = row.get(colName[4]).toString();
                student.put(colName[4], page);
                String attendance = row.get(colName[5]).toString();
                student.put(colName[5], attendance);
                String barrage = row.get(colName[7]).toString();
                student.put(colName[7], barrage);
                String submission = row.get(colName[8]).toString();
                student.put(colName[8], submission);
                String announcement = row.get(colName[9]).toString();
                student.put(colName[9], announcement);


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
        int totalAttendance = getNumberFromStr(colName[5]);
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

        // 释放资源
        stat.close();
        conn.close();
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
        int totalPoint = getNumberFromStr(colName[3]);
        int totalPage = getNumberFromStr(colName[4]);
        int totalAttendance = getNumberFromStr(colName[5]);
        int totalAnnouncement = getNumberFromStr(colName[9]);

        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Map<String,String> student = new LinkedHashMap<String,String>();
        String stuId = row.get(colName[1]).toString();
        student.put(colName[1], stuId);
        String name = row.get(colName[2]).toString();
        student.put(colName[2], name);
        String point = row.get(colName[3]).toString();
        student.put(colName[3], point);
        String page = row.get(colName[4]).toString();
        student.put(colName[4], page);
        String attendance = row.get(colName[5]).toString();
        student.put(colName[5], attendance);
        String barrage = row.get(colName[7]).toString();
        student.put(colName[7], barrage);
        String submission = row.get(colName[8]).toString();
        student.put(colName[8], submission);
        String announcement = row.get(colName[9]).toString();
        student.put(colName[9], announcement);

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

    /**
    public List<String> getTableNameByType(String courseName, String type) throws ClassNotFoundException, SQLException {
        List<String> list = new ArrayList<>();

        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        String sql = "select table_name from information_schema.tables where table_schema='project';";
        ResultSet rs = stat.executeQuery(sql);
        Result result = ResultSupport.toResult(rs);
        if(result!=null && result.getRowCount()!=0) {
            for (int i = 0; i < result.getRowCount(); i++) {
                Map row = result.getRows()[i];
                String table = row.get("table_name").toString();
                if(table.contains(courseName) && table.contains(type)) {
                    list.add(table);
                }
            }
        }

        // 释放资源
        stat.close();
        conn.close();
        return list;
    }
     **/

    public List<List<Integer>> getClassDataByTable(String courseName, List<String> tableList) throws ClassNotFoundException, SQLException {
        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        List<List<Integer>> list = new ArrayList<>();
        for(String tableName: tableList) {
            String sql = "select * from " + tableName + " ;";
            ResultSet rs = stat.executeQuery(sql);
            Result result = ResultSupport.toResult(rs);

            int attendance = result.getRowCount();     // 签到数
            int submission = 0;                        // 投稿
            int barrage = 0;                           // 弹幕
            int point = 0;                             // 答题


            List<Integer> data = new ArrayList<>();
            for (int i = 0; i < result.getRowCount(); i++) {
                Map row = result.getRows()[i];

                String attend = row.get("签到方式").toString();
                if (attend.equals("未上课")) {
                    attendance--;
                }

                String sub = row.get("投稿次数").toString();
                submission += Integer.parseInt(sub);

                String ba = row.get("弹幕次数").toString();
                barrage += Integer.parseInt(ba);

                String po = row.get("累计得分").toString();
                point += Integer.parseInt(po);
            }

            data.add(attendance);
            data.add(submission);
            data.add(barrage);
            data.add(point);

            list.add(data);
        }

        // 释放资源
        stat.close();
        conn.close();
        return list;
    }

    public List<List<Integer>> getBarDataOfClass(List<String> tableList) throws ClassNotFoundException, SQLException {
        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        Integer a1 = 0;     // 未上课
        Integer a2 = 0;     // 扫二维码
        Integer a3 = 0;     // 课堂暗号
        Integer a4 = 0;     // “正在上课”提示

        Integer p1 = 0;     // 未答题
        Integer p2 = 0;     // 答对
        Integer p3 = 0;     // 答错

        for(String table:tableList) {
            String sql = "select * from " + table + " ;";
            ResultSet rs = stat.executeQuery(sql);
            Result result = ResultSupport.toResult(rs);

            for (int i = 0; i < result.getRowCount(); i++) {
                Map row = result.getRows()[i];

                if(row.get("签到方式").toString().equals("未上课")) {
                    a1 ++;
                }
                else if(row.get("签到方式").toString().equals("扫二维码")) {
                    a2 ++;
                }
                else if(row.get("签到方式").toString().equals("课堂暗号")) {
                    a3 ++;
                }
                else  {
                    a4 ++;
                }

                // 如果老师有发题
                int cnt = result.getColumnNames().length;
                if(cnt > 10) {
                    String[] colName = result.getColumnNames();
                    // “总分”在colName[10] 则有cnt-11个题
                    if(row.get("总分").toString().equals(String.valueOf(cnt-11))) {   // 所有题答对
                        p2 = p2 + cnt - 11;
                    }
                    else {  // 不是所有题答对，可能答错，可能未答题   向右遍历
                        int temp = 0;   // 存储这一行未答题的数量
                        for(int j = 11; j < cnt; j ++) {
                            if(row.get(colName[j]).toString().equals("未答题")) {
                                temp ++;
                            }
                        }
                        p1 = p1 + temp;
                        p2 = p2 + Integer.parseInt(row.get("总分").toString());
                        p3 = p3 + cnt - Integer.parseInt(row.get("总分").toString()) - temp; // 答错=题数-答对数-未答数
                    }
                }
            }
        }

        List<Integer> attandance = new ArrayList<>();
        attandance.add(a1);
        attandance.add(a2);
        attandance.add(a3);
        attandance.add(a4);

        List<Integer> point = new ArrayList<>();
        point.add(p1);
        point.add(p2);
        point.add(p3);

        List<List<Integer>> list = new ArrayList<>();
        list.add(attandance);
        list.add(point);

        // 释放资源
        stat.close();
        conn.close();

        return list;
    }

    public List<List<Integer>> getLineDataOfBeforeClass(List<String> tableList) throws ClassNotFoundException, SQLException {
        List<List<Integer>> list = new ArrayList<>();

        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        List<Integer> pages = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();
        for(String table:tableList) {
            String sql = "select * from " + table + " ;";
            ResultSet rs = stat.executeQuery(sql);
            Result result = ResultSupport.toResult(rs);

            String[] colNames = result.getColumnNames();
            int totalPage = getNumberFromStr(colNames[3]);

            // 没有答题
            if(colNames.length <= 7) {
                Integer page = 0;
                for (int i = 0; i < result.getRowCount(); i++) {
                    Map row = result.getRows()[i];

                    page = page + Integer.parseInt(row.get(colNames[3]).toString());
                }
                page = (page*100) / (result.getRowCount()*totalPage);  // 均值除以总 再乘以100
                pages.add(page);

                scores.add(null);
            }
            else {
                int totalScore = getNumberFromStr(colNames[7]);

                Integer page = 0;
                Integer score = 0;
                for (int i = 0; i < result.getRowCount(); i++) {
                    Map row = result.getRows()[i];

                    page = page + Integer.parseInt(row.get(colNames[3]).toString());
                    score = score + Integer.parseInt(row.get(colNames[7]).toString());
                }

                page = (page * 100) / (result.getRowCount() * totalPage);  // 均值除以总 再乘以100
                pages.add(page);
                score = (score * 100) / (result.getRowCount() * totalScore);
                scores.add(score);
            }
        }

        // 释放资源
        stat.close();
        conn.close();

        list.add(pages);
        list.add(scores);
        return list;
    }

    public List<List<Integer>> getBarDataOfBeforeClass(List<String> tableList) throws ClassNotFoundException, SQLException {
        List<List<Integer>> list = new ArrayList<>();

        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        Integer a1 = 0;     // 未预习
        Integer a2 = 0;     // 部分预习
        Integer a3 = 0;     // 完成预习

        Integer p1 = 0;     // 未答题
        Integer p2 = 0;     // 答对
        Integer p3 = 0;     // 答错

        for(String table:tableList) {
            String sql = "select * from " + table + " ;";
            ResultSet rs = stat.executeQuery(sql);
            Result result = ResultSupport.toResult(rs);

            String[] colNames = result.getColumnNames();

            // 如果没有答题
            if(result.getColumnNames().length <= 7) {
                for (int i = 0; i < result.getRowCount(); i++) {
                    Map row = result.getRows()[i];

                    String pageState = row.get("完成时间").toString();
                    if(pageState.equals("未预习")) {
                        a1 ++;
                    }
                    else if(pageState.equals("未完成预习")) {
                        a2 ++;
                    }
                }
                a3 = a3 + result.getRowCount() - a1 - a2;
            }
            else {
                int pointTotal = result.getColumnNames().length - 8;

                for (int i = 0; i < result.getRowCount(); i++) {
                    Map row = result.getRows()[i];

                    String pageState = row.get("完成时间").toString();
                    if (pageState.equals("未预习")) {
                        a1++;
                    } else if (pageState.equals("未完成预习")) {
                        a2++;
                    }

                    int pointCurrent = Integer.parseInt(row.get(colNames[7]).toString());
                    p2 = p2 + pointCurrent;
                    if(pointTotal != pointCurrent) {
                        // 从下标为7开始 就是题目
                        int temp = 0;
                        for(int j = 8; j < colNames.length; j ++) {
                            if(row.get(colNames[j]).toString().equals("未答题")) {
                                temp ++;
                            }
                        }
                        p1 = p1 + temp;
                        p3 = p3 + pointTotal - pointCurrent - temp;
                    }

                }
                a3 = a3 + result.getRowCount() - a1 - a2;
            }
        }

        // 释放资源
        stat.close();
        conn.close();

        List<Integer> page = new ArrayList<>();
        page.add(a1);
        page.add(a2);
        page.add(a3);

        List<Integer> point = new ArrayList<>();
        point.add(p1);
        point.add(p2);
        point.add(p3);

        list.add(page);
        list.add(point);

        return list;
    }

    public List<Integer> getLineDataOfExam(List<String> tableList) throws ClassNotFoundException, SQLException {
        List<Integer> list = new ArrayList<>();

        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        for(String table:tableList) {
            String sql = "select * from " + table + " ;";
            ResultSet rs = stat.executeQuery(sql);
            Result result = ResultSupport.toResult(rs);

            String[] colNames = result.getColumnNames();
            int totalPoint = colNames.length - 5;

            Integer point = 0;
            for (int i = 0; i < result.getRowCount(); i++) {
                Map row = result.getRows()[i];
                point = point + Integer.parseInt(row.get(colNames[3]).toString());
            }
            list.add(point);
        }

        // 释放资源
        stat.close();
        conn.close();

        return list;
    }

    public List<List<Map<String,String>>> getClassList(List<String> tableList) throws ClassNotFoundException, SQLException {
        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        List<List<Map<String,String>>> list = new ArrayList<>();    // 存所有表的学生们
        for(String table:tableList) {
            String sql = "select * from " + table + " ;";
            ResultSet rs = stat.executeQuery(sql);
            Result result = ResultSupport.toResult(rs);

            List<Map<String,String>> students = new ArrayList<>();  // 存一张表的学生们
            for (int i = 0; i < result.getRowCount(); i++) {
                Map row = result.getRows()[i];
                // 对row进行处理 去掉后面的题目信息 保留关键信息
                Map<String,String> student = new LinkedHashMap<>();         // 存一张表的一个学生
                student.put("学号", row.get("学号").toString());
                student.put("姓名", row.get("姓名").toString());
                student.put("签到方式", row.get("签到方式").toString());
                student.put("签到时间", row.get("签到时间").toString());
                student.put("投稿次数", row.get("投稿次数").toString());
                student.put("弹幕次数", row.get("弹幕次数").toString());
                student.put("答题得分", row.get("累计得分").toString());
                students.add(student);
            }
            list.add(students);
        }

        // 释放资源
        stat.close();
        conn.close();

        return list;
    }

    public List<List<Map<String,String>>> getBeforeClassList(List<String> tableList) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        List<List<Map<String,String>>> list = new ArrayList<>();    // 存所有表的学生们
        for(String table:tableList) {
            String sql = "select * from " + table + " ;";
            ResultSet rs = stat.executeQuery(sql);
            Result result = ResultSupport.toResult(rs);
            String[] colNames = result.getColumnNames();

            List<Map<String,String>> students = new ArrayList<>();  // 存一张表的学生们
            for (int i = 0; i < result.getRowCount(); i++) {
                Map row = result.getRows()[i];
                // 对row进行处理 去掉后面的题目信息 保留关键信息
                Map<String,String> student = new LinkedHashMap<>();         // 存一张表的一个学生
                student.put("学号", row.get("学号").toString());
                student.put("姓名", row.get("姓名").toString());
                student.put(colNames[3], row.get(colNames[3]).toString());
                student.put("总时长", row.get("总时长").toString());
                student.put("完成时间", row.get("完成时间").toString());
                if(student.size() > 7) {
                    student.put(colNames[7], row.get(colNames[7]).toString());
                }
                students.add(student);
            }
            list.add(students);
        }

        stat.close();
        conn.close();

        return list;
    }

    public List<List<Map<String,String>>> getExamClassList(List<String> tableList) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        List<List<Map<String,String>>> list = new ArrayList<>();    // 存所有表的学生们
        for(String table:tableList) {
            String sql = "select * from " + table + " ;";
            ResultSet rs = stat.executeQuery(sql);
            Result result = ResultSupport.toResult(rs);
            String[] colNames = result.getColumnNames();

            List<Map<String,String>> students = new ArrayList<>();  // 存一张表的学生们
            for (int i = 0; i < result.getRowCount(); i++) {
                Map row = result.getRows()[i];
                // 对row进行处理 去掉后面的题目信息 保留关键信息
                Map<String,String> student = new LinkedHashMap<>();         // 存一张表的一个学生
                student.put("学号", row.get("学号").toString());
                student.put("姓名", row.get("姓名").toString());
                student.put(colNames[3], row.get(colNames[3]).toString());
                student.put("总用时", row.get("总用时").toString());
                student.put("交卷时间", row.get("交卷时间").toString());
                students.add(student);
            }
            list.add(students);
        }

        stat.close();
        conn.close();

        return list;
    }
}

