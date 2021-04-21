package com.example.demo.utils;


import com.example.demo.vo.SheetContent;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class SqlUtil {
    // 对应数据库配置 在application.properties
    private String driver = "com.mysql.jdbc.Driver";

    private String url = "jdbc:mysql://127.0.0.1:3306/project?characterEncoding=UTF-8";

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
    }

    public void updateByAdd(String course, List<String> tableNameList, List<String[]> headersList, List<List<Map<String, String>>> tableContentList) throws ClassNotFoundException, SQLException {
        // 连接数据库
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement stat = conn.createStatement();

        // todo
    }

}

