package com.example.demo.controller;

import com.example.demo.service.StatisticsService;
import com.example.demo.vo.ChartResult;
import com.example.demo.vo.StatListResult;
import com.example.demo.vo.OneDataResult;
import com.example.demo.vo.TwoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

@Controller
public class StatisticsController {
    @Autowired
    StatisticsService statisticsService;

    @CrossOrigin
    @GetMapping(value = "api/statistics/onClass/line")
    @ResponseBody
    public ChartResult getClassLine(@RequestParam("coursename")String courseName) throws SQLException, ClassNotFoundException {
        return statisticsService.getClassLine(courseName);
    }

    @CrossOrigin
    @GetMapping(value = "api/statistics/onClass/bar")
    @ResponseBody
    public TwoDataResult getClassBar(@RequestParam("coursename")String courseName) throws SQLException, ClassNotFoundException {
        return statisticsService.getClassBar(courseName);
    }

    @CrossOrigin
    @GetMapping(value = "api/statistics/onClass/list")
    @ResponseBody
    public StatListResult getClassList(@RequestParam("coursename")String courseName,
                                       @RequestParam("num")int num,
                                       @RequestParam("stuID")String stuID,
                                       @RequestParam("stuName")String stuName,
                                       @RequestParam("attendType")String attendType,
                                       @RequestParam("submission")String submission,
                                       @RequestParam("barrage")String barrage,
                                       @RequestParam("point")String point,
                                       @RequestParam("pagenum")int pagenum,
                                       @RequestParam("pagesize")int pagesize) throws SQLException, ClassNotFoundException {
        return statisticsService.getClassList(courseName,num,stuID, stuName, attendType, submission, barrage, point,pagenum,pagesize);
    }

    @CrossOrigin
    @GetMapping(value = "api/statistics/beforeClass/line")
    @ResponseBody
    public TwoDataResult getBeforeClassLine(@RequestParam("coursename")String courseName) throws SQLException, ClassNotFoundException {
        return statisticsService.getBeforeClassLine(courseName);
    }

    @CrossOrigin
    @GetMapping(value = "api/statistics/beforeClass/bar")
    @ResponseBody
    public TwoDataResult getBeforeClassBar(@RequestParam("coursename")String courseName) throws SQLException, ClassNotFoundException {
        return statisticsService.getBeforeClassBar(courseName);
    }

    @CrossOrigin
    @GetMapping(value = "api/statistics/beforeClass/list")
    @ResponseBody
    public StatListResult getBeforeClassList(@RequestParam("coursename")String courseName,
                                       @RequestParam("num")int num,
                                       @RequestParam("stuID")String stuID,
                                       @RequestParam("stuName")String stuName,
                                       @RequestParam("page")String page,
                                       @RequestParam("totalTime")String totalTime,
                                       @RequestParam("endTime")String endTime,
                                       @RequestParam("point")String point,
                                       @RequestParam("pagenum")int pagenum,
                                       @RequestParam("pagesize")int pagesize) throws SQLException, ClassNotFoundException {
        return statisticsService.getBeforeClassList(courseName,num,stuID, stuName, page,point,totalTime,endTime,pagenum,pagesize);
    }


    @CrossOrigin
    @GetMapping(value = "api/statistics/exam/line")
    @ResponseBody
    public OneDataResult getExamLine(@RequestParam("coursename")String courseName) throws SQLException, ClassNotFoundException {
        return statisticsService.getExamLine(courseName);
    }

    @CrossOrigin
    @GetMapping(value = "api/statistics/exam/list")
    @ResponseBody
    public StatListResult getExamClassList(@RequestParam("coursename")String courseName,
                                       @RequestParam("num")int num,
                                       @RequestParam("stuID")String stuID,
                                       @RequestParam("stuName")String stuName,
                                       @RequestParam("point")String point,
                                       @RequestParam("totalTime")String totalTime,
                                       @RequestParam("endTime")String endTime,
                                       @RequestParam("pagenum")int pagenum,
                                       @RequestParam("pagesize")int pagesize) throws SQLException, ClassNotFoundException {
        return statisticsService.getExamClassList(courseName,num,stuID, stuName, point,totalTime,endTime,pagenum,pagesize);
    }
}
