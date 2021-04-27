package com.example.demo.service;

import com.example.demo.utils.SqlUtil;
import com.example.demo.vo.ScoreResult;
import com.example.demo.vo.StudentResult;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreService {
    public ScoreResult getScoreList(String courseName, String stuID, Map<Object,Object> setting, int pagenum, int pagesize) throws SQLException, ClassNotFoundException {
        // System.out.println(setting);
        ScoreResult scoreResult = new ScoreResult();
        SqlUtil sqlUtil = new SqlUtil();

        if(stuID.isEmpty()){
            scoreResult = sqlUtil.getScoreList(courseName, setting);
        }
        else {
            scoreResult = sqlUtil.getScoreById(courseName, setting, stuID);
        }

        return scoreResult;
    }

    public ScoreResult getScoreListByFilter(String point,String page,String attendance,String barrage,String submission,String announcement, String average, String courseName, String stuID, Map<Object,Object> setting, int pagenum, int pagesize) throws SQLException, ClassNotFoundException {
        ScoreResult scoreResult = new ScoreResult();
        scoreResult.setCode(400);

        // 调用getScoreList  得到所有list
        ScoreResult scores = new ScoreResult();
        scores = getScoreList(courseName, stuID, setting, pagenum, pagesize);
        List<Map<String,String>> scoreList = scores.getScores();

        SqlUtil sqlUtil = new SqlUtil();
        String[] colName = sqlUtil.getColName(courseName);

        if(!point.isEmpty()) {
            // 遍历list  取反删除来实现
            scoreList.removeIf(score -> Integer.parseInt(score.get(colName[2])) >= Integer.parseInt(point));
        }
        if(!page.isEmpty()) {
            scoreList.removeIf(score -> Integer.parseInt(score.get(colName[3])) >= Integer.parseInt(page));
        }
        if(!attendance.isEmpty()) {  // 缺勤次数多于x 即 签到次数小于总-x 再取反
            int totalAttendance = sqlUtil.getTotalAttendance(courseName);
            scoreList.removeIf(score -> Integer.parseInt(score.get(colName[4])) >= (totalAttendance - Integer.parseInt(attendance)));
        }
        if(!barrage.isEmpty()) {
            scoreList.removeIf(score -> Integer.parseInt(score.get(colName[6])) >= Integer.parseInt(barrage));
        }
        if(!submission.isEmpty()) {
            scoreList.removeIf(score -> Integer.parseInt(score.get(colName[7])) >= Integer.parseInt(submission));
        }
        if(!announcement.isEmpty()) {
            scoreList.removeIf(score -> Integer.parseInt(score.get(colName[8])) >= Integer.parseInt(announcement));
        }
        if(!average.isEmpty()) {
            scoreList.removeIf(score -> Integer.parseInt(score.get("平时成绩")) >= Integer.parseInt(average));
        }

        if(scoreList.size() != 0) {
            scoreResult.setCode(200);
        }
        scoreResult.setScores(scoreList);
        scoreResult.setTotal(scoreList.size());

        return scoreResult;
    }
}
