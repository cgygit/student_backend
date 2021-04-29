package com.example.demo.controller;

import com.example.demo.service.StatisticsService;
import com.example.demo.vo.ChartResult;
import com.example.demo.vo.ClassBarResult;
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
    public ClassBarResult getClassBar(@RequestParam("coursename")String courseName) throws SQLException, ClassNotFoundException {
        return statisticsService.getClassBar(courseName);
    }
}
