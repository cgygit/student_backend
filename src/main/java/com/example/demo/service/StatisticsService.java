package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.CourseDao;
import com.example.demo.entity.Course;
import com.example.demo.utils.SqlUtil;
import com.example.demo.vo.ChartResult;
import com.example.demo.vo.StatListResult;
import com.example.demo.vo.OneDataResult;
import com.example.demo.vo.TwoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {
    @Autowired
    CourseDao courseDao;

    public ChartResult getClassLine(String courseName) throws SQLException, ClassNotFoundException {
        ChartResult chartResult = new ChartResult();
        chartResult.setCode(200);

        Course course = courseDao.findByName(courseName);
        String lesson = course.getLesson();
        lesson = lesson.substring(0, lesson.length()-1);  // 去掉最后的逗号
        String[] lessons = lesson.split(",");
        List<String> tableList = Arrays.asList(lessons);

        String xAxis = "xAxis:{axisLabel:{show:true, interval:0,rotate:60}, data:[";
        String s1 = "{ name:'签到', type:'line', data:[";
        String s2 = "{ name:'投稿', type:'line', data:[";
        String s3 = "{ name:'弹幕', type:'line', data:[";
        String s4 = "{ name:'答题', type:'line', data:[";

        // 为了防止"Too many connections" 传入列表
        SqlUtil sqlUtil = new SqlUtil();
        List<List<Integer>> allTableData = sqlUtil.getClassDataByTable(courseName, tableList);

//        for(int i = 0; i <tableList.size(); i ++) {
//            System.out.println(tableList.get(i));
//            System.out.println(allTableData.get(i));
//        }

        for(String table: tableList) {
            // 修改表名
            String[] t = table.split("_");
            table = t[2];
            xAxis = xAxis + "\"" + table + "\",";
        }

        for(List<Integer> tableData:allTableData) {
            // 绘制四条线
            s1 = s1 + tableData.get(0) + ",";
            s2 = s2 + tableData.get(1) + ",";
            s3 = s3 + tableData.get(2) + ",";
            s4 = s4 + tableData.get(3) + ",";
        }

        xAxis = xAxis + "] },";
        s1 = s1 + "] },";
        s2 = s2 + "] },";
        s3 = s3 + "] },";
        s4 = s4 + "] },";



        String lineStr = "{title:{text: '课堂情况各项数据走势', left:'center'}," +
                "tooltip: {trigger: 'axis'}," +
                "legend: {data: ['签到', '投稿', '弹幕', '答题'],top: 40}," +
                "grid: { y: 110, y2:140 }," +
                 xAxis +
                "yAxis:{}, " +
                "series:[" + s1 + s2 + s3 + s4 + "]," +
                "color:['rgb(21,13,125,0.57)', 'rgb(46,41,107,0.91)', 'rgb(216,184,219,0.7)', 'rgb(173,207,240,0.8)', ]}";
        JSONObject obj= JSON.parseObject(lineStr);

        chartResult.setData(obj);

        return chartResult;
    }

    public TwoDataResult getClassBar(String courseName) throws SQLException, ClassNotFoundException {
        TwoDataResult twoDataResult = new TwoDataResult();
        twoDataResult.setCode(200);

        Course course = courseDao.findByName(courseName);
        String lesson = course.getLesson();
        lesson = lesson.substring(0, lesson.length()-1);  // 去掉最后的逗号
        String[] lessons = lesson.split(",");
        List<String> tableList = Arrays.asList(lessons);


        SqlUtil sqlUtil = new SqlUtil();
        List<List<Integer>> barDataList = sqlUtil.getBarDataOfClass(tableList);
        List<Integer> attandance = barDataList.get(0);
        List<Integer> point = barDataList.get(1);

//        String bar1 = "{title:{text: '签到方式汇总', left:'center'}," +
//                "xAxis: { data: ['未上课', '扫二维码', '课堂暗号', '“正在上课”提示'] }," +
//                "yAxis:{type: 'value', axisLabel:{ formatter: function (value) { let item=''; if(value==250){item='1000'} return item }  }, }, " +
//                "series: [{ data: [" + attandance.get(0) + "," + attandance.get(1) + "," + attandance.get(2) + "," + attandance.get(3) + "], type: 'bar' }] }";
//
//        JSONObject obj1=JSON.parseObject(bar1);
//
//        String bar2 = "{title:{text: '答题情况汇总', left:'center'}," +
//                "xAxis: { data: ['未答题', '答题正确', '答题错误'] }, " +
//                "yAxis:{type: 'value', axisLabel:{ formatter: function (value){ let item=''; if(value==1000){item='8000'} return item }  }, }, " +
//                "series: [{ data: [" + point.get(0) + "," + point.get(1) + "," + point.get(2)  + "], type: 'bar' }] }";
//
//        JSONObject obj2=JSON.parseObject(bar2);

        twoDataResult.setData1(attandance);
        twoDataResult.setData2(point);

        return twoDataResult;
    }

    public TwoDataResult getBeforeClassLine(String courseName) throws SQLException, ClassNotFoundException {
        TwoDataResult twoDataResult = new TwoDataResult();
        twoDataResult.setCode(200);

        Course course = courseDao.findByName(courseName);
        String readiness = course.getReadiness();
        readiness = readiness.substring(0, readiness.length()-1);  // 去掉最后的逗号
        String[] readinesses = readiness.split(",");
        List<String> tableList = Arrays.asList(readinesses);

        SqlUtil sqlUtil = new SqlUtil();
        List<List<Integer>> list = sqlUtil.getLineDataOfBeforeClass(tableList);
        twoDataResult.setData1(list.get(0));
        twoDataResult.setData2(list.get(1));

        // 修改表名 作为x轴
        List<String> tables = new ArrayList<>();
        for(String table:tableList) {
            String[] t = table.split("_");
            table = t[2];
            tables.add(table);
        }
        twoDataResult.setxAxis(tables);

        return twoDataResult;
    }

    public TwoDataResult getBeforeClassBar(String courseName) throws SQLException, ClassNotFoundException {
        TwoDataResult twoDataResult = new TwoDataResult();
        twoDataResult.setCode(200);

        Course course = courseDao.findByName(courseName);
        String readiness = course.getReadiness();
        readiness = readiness.substring(0, readiness.length()-1);  // 去掉最后的逗号
        String[] readinesses = readiness.split(",");
        List<String> tableList = Arrays.asList(readinesses);

        SqlUtil sqlUtil = new SqlUtil();
        List<List<Integer>> list = sqlUtil.getBarDataOfBeforeClass(tableList);
        twoDataResult.setData1(list.get(0));
        twoDataResult.setData2(list.get(1));

        twoDataResult.setxAxis(null);

        return twoDataResult;
    }

    public OneDataResult getExamLine(String courseName) throws SQLException, ClassNotFoundException {
        OneDataResult oneDataResult = new OneDataResult();
        oneDataResult.setCode(200);

        Course course = courseDao.findByName(courseName);
        String exam = course.getExam();
        exam = exam.substring(0, exam.length()-1);  // 去掉最后的逗号
        String[] exams = exam.split(",");
        List<String> tableList = Arrays.asList(exams);

        SqlUtil sqlUtil = new SqlUtil();
        List<Integer> list = sqlUtil.getLineDataOfExam(tableList);
        oneDataResult.setData(list);

        List<String> tables = new ArrayList<>();
        for(String table:tableList) {
            String[] t = table.split("_");
            table = t[2];
            tables.add(table);
        }
        oneDataResult.setxAxis(tables);

        return oneDataResult;
    }

    public StatListResult getClassList(String courseName, int num,String stuID,String stuName,String attendType,String submission,String barrage,String point, int pagenum, int pagesize) throws SQLException, ClassNotFoundException {
        StatListResult statListResult = new StatListResult();
        statListResult.setCode(200);

        // 获得所有的课堂情况表名
        Course course = courseDao.findByName(courseName);
        String lesson = course.getLesson();
        lesson = lesson.substring(0, lesson.length()-1);
        String[] lessons = lesson.split(",");
        List<String> tableList = Arrays.asList(lessons);
        int total = tableList.size();
        statListResult.setTotal(total);

        // 获得所有课堂情况的表
        SqlUtil sqlUtil = new SqlUtil();
        List<List<Map<String,String>>> list = sqlUtil.getClassList(tableList);

        // 获得第几张表 注意num比下标多1
        List<Map<String,String>> students = list.get(num-1);

        // 过滤器
        if(!stuID.isEmpty()) {
            for(Map<String,String> student:students) {
                if(!student.get("学号").equals(stuID)){
                    students.remove(student);
                }
            }
        }

        if(!stuName.isEmpty()) {
            for(Map<String,String> student:students) {
                if(!student.get("姓名").equals(stuName)){
                    students.remove(student);
                }
            }
        }

        if(!attendType.isEmpty()) {
            for(Map<String,String> student:students) {
                if(!student.get("签到方式").equals(attendType)){
                    students.remove(student);
                }
            }
        }

        if(!submission.isEmpty()) {
            for(Map<String,String> student:students) {
                if(Integer.parseInt(student.get("投稿次数")) >= Integer.parseInt(submission)) {
                    students.remove(student);
                }
            }
        }

        if(!barrage.isEmpty()) {
            for(Map<String,String> student:students) {
                if(Integer.parseInt(student.get("弹幕次数")) >= Integer.parseInt(barrage)) {
                    students.remove(student);
                }
            }
        }

        if(!point.isEmpty()) {
            for(Map<String,String> student:students) {
                if(Integer.parseInt(student.get("答题得分")) >= Integer.parseInt(point)) {
                    students.remove(student);
                }
            }
        }

        statListResult.setStudents(students);

        return statListResult;
    }

    public StatListResult getBeforeClassList(String courseName,int num,String stuID,String stuName,String page,String totalTime,String endTime,String point,int pagenum,int pagesize) throws SQLException, ClassNotFoundException {
        StatListResult statListResult = new StatListResult();
        statListResult.setCode(200);

        Course course = courseDao.findByName(courseName);
        String readiness = course.getReadiness();
        readiness = readiness.substring(0, readiness.length()-1);
        String[] readinesses = readiness.split(",");
        List<String> tableList = Arrays.asList(readinesses);
        int total = tableList.size();
        statListResult.setTotal(total);

        SqlUtil sqlUtil = new SqlUtil();
        List<List<Map<String,String>>> list = sqlUtil.getBeforeClassList(tableList);
        List<Map<String,String>> students = list.get(num-1);

        if(!stuID.isEmpty()) {
            for(Map<String,String> student:students) {
                if(!student.get("学号").equals(stuID)){
                    students.remove(student);
                }
            }
        }

        if(!stuName.isEmpty()) {
            for(Map<String,String> student:students) {
                if(!student.get("姓名").equals(stuName)){
                    students.remove(student);
                }
            }
        }

        if(!page.isEmpty()) {
            for(Map<String,String> student:students) {
                // 观看总页数_共xx页 在我构建的有序map中为2
                if(Integer.parseInt(student.get(2)) >= Integer.parseInt(page)) {
                    students.remove(student);
                }
            }
        }

        if(!point.isEmpty()) {
            for(Map<String,String> student:students) {
                // 要有答题得分才能比 没有的不管  有答题得分即student.size()>5
                if(student.size() <= 5) {
                    students.remove(student);
                }
                else if(Integer.parseInt(student.get(5)) >= Integer.parseInt(point)) {
                    students.remove(student);
                }
            }
        }

        return statListResult;
    }

    public StatListResult getExamClassList(String courseName,int num,String stuID,String stuName,String point,String totalTime,String endTime,int pagenum,int pagesize) throws SQLException, ClassNotFoundException {
        StatListResult statListResult = new StatListResult();
        statListResult.setCode(200);

        Course course = courseDao.findByName(courseName);
        String exam = course.getReadiness();
        exam = exam.substring(0, exam.length()-1);
        String[] exams = exam.split(",");
        List<String> tableList = Arrays.asList(exams);
        int total = tableList.size();
        statListResult.setTotal(total);

        SqlUtil sqlUtil = new SqlUtil();
        List<List<Map<String,String>>> list = sqlUtil.getExamClassList(tableList);
        List<Map<String,String>> students = list.get(num-1);

        if(!stuID.isEmpty()) {
            for(Map<String,String> student:students) {
                if(!student.get("学号").equals(stuID)){
                    students.remove(student);
                }
            }
        }

        if(!stuName.isEmpty()) {
            for(Map<String,String> student:students) {
                if(!student.get("姓名").equals(stuName)){
                    students.remove(student);
                }
            }
        }

        if(!point.isEmpty()) {
            for(Map<String,String> student:students) {
                // 得分_共xx分 在我构建的有序map中为2
                if(Integer.parseInt(student.get(2)) >= Integer.parseInt(point)) {
                    students.remove(student);
                }
            }
        }

        return statListResult;
    }
}
