package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.example.demo.utils.SqlUtil;
import com.example.demo.vo.BtnResult;
import com.example.demo.vo.ChartResult;
import com.example.demo.vo.StudentResult;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StudyService {

    public List<Map<String,String>> getClass(String courseName) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        SqlUtil sqlUtil = new SqlUtil();
        StudentResult studentResult = sqlUtil.getStudentList(courseName);
        List<Map<String,String>> studentList = studentResult.getStudents();

        // 在后端处理一下数据 去掉中文 只保留值 方便python脚本处理数据
        List<List<String>> argList = new ArrayList<List<String>>();
        for (Map<String,String> map: studentList) {
            //Object mapValue = map.values();
            List<String> mapValue = new ArrayList<String>(map.values());
            mapValue.remove(0);
            argList.add(mapValue);
        }
        // System.out.println(argList);
        // 查看错误
//        String[] args1 = new String[] { "D:\\python\\python.exe", "C:\\Users\\Candelion\\Desktop\\Student\\sklearn_demo\\src\\KMeansModel.py", argList.toString()};
//        Process process = Runtime.getRuntime().exec(args1);
//        new InputStreamRunnable(process.getErrorStream(), "Error").start();
//        new InputStreamRunnable(process.getInputStream(), "Info").start();
//
//        process.waitFor();
//        process.destroy();

        String dataClass = "";
        try {
            String[] args1 = new String[] { "D:\\python\\python.exe", "C:\\Users\\Candelion\\Desktop\\Student\\sklearn_demo\\src\\KMeansModel.py", argList.toString()};
            Process pr = Runtime.getRuntime().exec(args1);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    pr.getInputStream(),"gbk"));
            String line;
            while ((line = in.readLine()) != null) {
                // System.out.println(line);
                dataClass = dataClass + line;
            }
            in.close();
            pr.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(dataClass);
        // [0 1 3 1 1 0 1 3 2 2 2 3 0 0 3 0 0 3 3 3 2 0 2 2 3 0 0 2 0 2 2 0 0 3 0 0 2 0 3 0 3 3 0 3 2 0 0 1 0 0 2 2 3 2 3 0 1 0 0 0 2 0 0 2 0 0 3 2 2 3 3 2 2 2 0 3 3 3 0 3 1 3 1 3 3 3 1 3 3 0 0 0 3 0 0 3]

        // 把类别加到studentList中
        String[] classList;
        dataClass.substring(2, dataClass.length()-2);
        classList = dataClass.split(" ");
        for(int i = 0; i < studentList.size(); i ++) {
            studentList.get(i).put("类别", classList[i]);
        }
        return studentList;
    }



    public ChartResult getClassByPie(String courseName) throws ClassNotFoundException, SQLException, InterruptedException, IOException {
        ChartResult pieResult = new ChartResult();
        pieResult.setCode(200);
        List<Map<String,String>> studentList = getClass(courseName);


        int num0 = 0;
        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        for(Map<String,String> map: studentList) {
            if(map.get("类别").isEmpty()) {
                pieResult.setCode(400);
            }
            // 计算每类的数量
            if(map.get("类别").equals("0")) {
                num0 ++;
            }
            if(map.get("类别").equals("1")) {
                num1 ++;
            }
            if(map.get("类别").equals("2")) {
                num2 ++;
            }
            if(map.get("类别").equals("3")) {
                num3 ++;
            }
        }

        String pieStr = "{ title:{text: '学生学业类别', left:'center'},tooltip: {trigger: 'item'}, legend: {orient: 'vertical',right:0,},  " +
                        "grid: { y: 110}," +
                        "series : [ { name: '学生学业类别', type: 'pie', radius: '60%'," +
                        " data:[ {value:" + num0 + ", name:'类别一'}," +
                                "{value:" + num1 + ", name:'类别二'}," +
                                "{value:" + num2 + ", name:'类别三'}," +
                                "{value:" + num3 + ", name:'类别四'}, ]," +
                        "emphasis: { itemStyle: {shadowBlur: 10,shadowOffsetX: 0,shadowColor: 'rgba(0, 0, 0, 0.5)' } } } ]," +
                        "color:['rgb(58,71,133,0.82)', 'rgb(46,41,107,0.91)', 'rgb(216,184,219,0.7)', 'rgb(173,207,240,0.8)', ] }";
        JSONObject obj=JSON.parseObject(pieStr);

        // System.out.println(obj);
        pieResult.setData(obj);

        return pieResult;
    }

    public List<int[]> getClassData(String courseName) throws ClassNotFoundException, SQLException, InterruptedException, IOException {
        List<Map<String,String>> studentList = getClass(courseName);
        SqlUtil sqlUtil = new SqlUtil();
        String[] colName = sqlUtil.getColName(courseName);


        int[] point = {0,0,0,0};        // point 四类学生 0 1 2 3 初始化为0  对应colName[2]
        int[] page = {0,0,0,0};         // colName[3]
        int[] attendance = {0,0,0,0};   // colName[4]
        int[] barrage = {0,0,0,0};      // colName[6]
        int[] submission = {0,0,0,0};   // colName[7]
        int[] announcement = {0,0,0,0}; // colName[8]
        int[] cnt = {0,0,0,0};          // 记录每一类有多少个学生

        for (Map<String,String> map: studentList) {
            for (int i = 0; i < 4; i ++) {
                if (map.get("类别").equals(String.valueOf(i))) {
                    point[i] += Integer.parseInt(map.get(colName[2]));
                    page[i] += Integer.parseInt(map.get(colName[3]));
                    attendance[i] += Integer.parseInt(map.get(colName[4]));
                    barrage[i] += Integer.parseInt(map.get(colName[6]));
                    submission[i] += Integer.parseInt(map.get(colName[7]));
                    announcement[i] += Integer.parseInt(map.get(colName[8]));
                    cnt[i] ++;
                }
            }
        }
        List<int[]> list = new ArrayList<>();
        list.add(point);
        list.add(page);
        list.add(attendance);
        list.add(barrage);
        list.add(submission);
        list.add(announcement);
        list.add(cnt);
        return list;
    }

    // 四类学生 各项的 平均值
    public ChartResult getClassByLine(String courseName) throws ClassNotFoundException, SQLException, InterruptedException, IOException {
        ChartResult chartResult = new ChartResult();

        List<int[]> list = getClassData(courseName);
        List<int[]> averageList = new ArrayList<>();

        int[] cnt = list.get(6);
        for(int i = 0; i < 6; i ++) {   // 对每一项
            int[] average = {0,0,0,0};  // 均值有4类
            int[] data = list.get(i);
            for(int j = 0; j < 4; j++) {
                average[j] = data[j]/cnt[j];
            }
            averageList.add(average);
        }

        String lineStr = "{title:{text: '四类学生各项指标的平均值', left:'center'}," +
                    "tooltip: {trigger: 'axis'}," +
                    "legend: {data: ['类别一', '类别二', '类别三', '类别四'],top: 40}," +
                    "grid: { y: 110}," +
                    "xAxis:{ data:[\"答题得分\",\"观看页数\",\"签到\",\"弹幕\",\"投稿\",\"阅读公告数\"] }," +
                    "yAxis:{}, " +
                    "series:[{ name:'类别一', type:'line', data:[" + averageList.get(0)[0] + "," + averageList.get(1)[0] + "," + averageList.get(2)[0] + "," +
                                                                    averageList.get(3)[0] + "," + averageList.get(4)[0] + "," + averageList.get(5)[0] + "] }," +
                            "{ name:'类别二', type:'line', data:[" + averageList.get(0)[1] + "," + averageList.get(1)[1] + "," + averageList.get(2)[1] + "," +
                                                                    averageList.get(3)[1] + "," + averageList.get(4)[1] + "," + averageList.get(5)[1] + "] }," +
                            "{ name:'类别三', type:'line', data:[" + averageList.get(0)[2] + "," + averageList.get(1)[2] + "," + averageList.get(2)[2] + "," +
                                                                    averageList.get(3)[2] + "," + averageList.get(4)[2] + "," + averageList.get(5)[2] + "] }," +
                            "{ name:'类别四', type:'line', data:[" + averageList.get(0)[3] + "," + averageList.get(1)[3] + "," + averageList.get(2)[3] + "," +
                                                                    averageList.get(3)[3] + "," + averageList.get(4)[3] + "," + averageList.get(5)[3] + "] }]," +
                    "color:['rgb(58,71,133,0.82)', 'rgb(46,41,107,0.91)', 'rgb(216,184,219,0.7)', 'rgb(173,207,240,0.8)', ]}";
        JSONObject obj=JSON.parseObject(lineStr);
        // System.out.println(obj);
        chartResult.setCode(200);
        chartResult.setData(obj);

        return chartResult;
    }

    public BtnResult getBtnData(String coursename) throws ClassNotFoundException, SQLException, InterruptedException, IOException {
        BtnResult btnResult = new BtnResult();
        btnResult.setCode(200);

        List<int[]> data = getClassData(coursename);
        int[] cnt = data.get(6);

        int total = 0;
        for(int i = 0; i < 4; i++) {
            total += cnt[i];
        }

        btnResult.setBtn1(String.valueOf(total));
        btnResult.setBtn2(String.valueOf(cnt[0]));
        btnResult.setBtn3(String.valueOf(cnt[1]));
        btnResult.setBtn4(String.valueOf(cnt[2]));
        btnResult.setBtn5(String.valueOf(cnt[3]));

        return btnResult;
    }

    public StudentResult getStudentList(String courseName,String type,int pagenum,int pagesize) throws ClassNotFoundException, SQLException, InterruptedException, IOException {
        StudentResult studentResult = new StudentResult();
        studentResult.setCode(200);
        List<Map<String,String>> students = new ArrayList<>();

        if(type.equals("4")) {
            SqlUtil sqlUtil = new SqlUtil();
            studentResult = sqlUtil.getStudentList(courseName);
        }
        else {
            List<Map<String, String>> studentList = getClass(courseName);
            for (Map<String, String> student : studentList) {
                if (student.get("类别").equals(type)) {
                    student.remove("类别");
                    students.add(student);
                }
            }
            studentResult.setStudents(students);
            studentResult.setTotal(students.size());
        }
        return studentResult;
    }

}
