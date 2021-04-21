package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.utils.ExcelImportUtil;
import com.example.demo.utils.PageUtil;
import com.example.demo.utils.SqlUtil;
import com.example.demo.vo.Course;
import com.example.demo.vo.ListResult;
import com.example.demo.vo.Result;
import com.example.demo.vo.SheetContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class FileService {
    @Autowired
    UserDao userDao;
    @Autowired
    PageUtil pageUtil;

    public Result importFile(MultipartFile file, String coursename, String classname, String username) throws Exception {
        // 本地获得数据
        ExcelImportUtil importUtil = new ExcelImportUtil(file);                     // 读取excel
        SheetContent sumContent = importUtil.readExcelSumContent();                 // 对应一张表
        List<List<SheetContent>> allSubContent = importUtil.readExcelSubContent();  // 每类n张sheet 分成三类
        // 加上前缀 课名_班名
        String course0 = sumContent.getSheetName();
        course0 = coursename + "_" +classname + "_" +course0;
        sumContent.setSheetName(course0);
        for (int i = 0; i < 3; i ++) {
            List<SheetContent> subContent = allSubContent.get(i);
            for (SheetContent sub: subContent) {
                String course1 = sub.getSheetName();
                course1 = coursename + "_" +classname + "_" +course1;
                sub.setSheetName(course1);
            }
        }
        // System.out.println(sumContent.getSheetContent());
        // System.out.println(sumContent.getSheetName());
        // System.out.println("--------------------------------------------");
        // System.out.println(allSubContent.get(1).get(0).getSheetContent());
        // System.out.println(allSubContent.get(1).get(0).getSheetName());

        // 导入到数据库中
        SqlUtil sqlUtil = new SqlUtil();
        List<String> tableNameList = new ArrayList<String>();
        List<String[]> headersList = new ArrayList<String[]>();
        List<List<Map<String, String>>> tableContentList = new ArrayList<List<Map<String, String>>>();
        tableNameList.add(sumContent.getSheetName());
        headersList.add(sumContent.getHeaders());
        tableContentList.add(sumContent.getSheetContent());
        for (int i = 0; i < 3; i ++) {
            List<SheetContent> subContent = allSubContent.get(i);
            for (int j = 0; j < subContent.size(); j ++) {
                String tableName = subContent.get(j).getSheetName();
                String[] headers = subContent.get(j).getHeaders();
                List<Map<String, String>> tableContent = subContent.get(j).getSheetContent();
                tableNameList.add(tableName);
                headersList.add(headers);
                tableContentList.add(tableContent);
            }
        }
        sqlUtil.initTable(tableNameList, headersList, tableContentList);

        // 更新user表
        String course = coursename + "_" +classname;
        sqlUtil.updateUser(course, username);

        if(importUtil == null) {
            return new Result(400);
        }
        else {
            return new Result(200);
        }
    }

    // 获取课程列表
    public ListResult getFileList(String username, int pagenum, int pagesize) {
        User user = userDao.findByUsername(username);
        String course = user.getCourse();
        String[] str = course.split(",");
        List<Course> list = new ArrayList<Course>();
        for (int i = 0; i < str.length; i ++) {
            Course course1 = new Course(str[i]);
            list.add(course1);
        }
        int total = str.length;
        list = pageUtil.startPage(list, pagenum, pagesize);
        return new ListResult(list, total);
    }

    // 删除课程
    public Result removeFile(String fileName, String userName) throws SQLException, ClassNotFoundException {
        // 删除以fileName为前缀的表
        SqlUtil sqlUtil = new SqlUtil();
        boolean flag = sqlUtil.removeTables(fileName);
        // 删除user表中course字段的该文件名
        User user = userDao.findByUsername(userName);
        String newCourse = user.getCourse().replace(fileName+",", "");
        user.setCourse(newCourse);
        userDao.save(user);

        if (flag == true) {
            return new Result(200);
        }
        else {
            return new Result(400);
        }
    }

    // 更新课程
    public Result updateFile(MultipartFile file, String course, String type) throws Exception {
        ExcelImportUtil importUtil = new ExcelImportUtil(file);
        SheetContent sumContent = importUtil.readExcelSumContent();
        List<List<SheetContent>> allSubContent = importUtil.readExcelSubContent();

        SqlUtil sqlUtil = new SqlUtil();
        if (type.equals("替换")) {
            // 删去原来的汇总 ；全部放进list  插入
            List<String> tableNameList = new ArrayList<String>();
            List<String[]> headersList = new ArrayList<String[]>();
            List<List<Map<String, String>>> tableContentList = new ArrayList<List<Map<String, String>>>();

            String course0 = sumContent.getSheetName();
            course0 = course + "_" +course0;
            tableNameList.add(course0);
            headersList.add(sumContent.getHeaders());
            tableContentList.add(sumContent.getSheetContent());

            for (int i = 0; i < 3; i ++) {
                List<SheetContent> subContent = allSubContent.get(i);
                for (int j = 0; j < subContent.size(); j ++) {
                    String tableName = subContent.get(j).getSheetName();
                    tableName = course + "_" +tableName;
                    String[] headers = subContent.get(j).getHeaders();
                    List<Map<String, String>> tableContent = subContent.get(j).getSheetContent();
                    tableNameList.add(tableName);
                    headersList.add(headers);
                    tableContentList.add(tableContent);
                }
            }

            sqlUtil.updateByReplace(course, tableNameList, headersList, tableContentList);
        }
        else if (type.equals("追加")) {
            // 查到原来的汇总 ； 加上这个汇总 ； 插入子表
            // todo
        }

        return new Result(200);
    }

}
