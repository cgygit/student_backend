package com.example.demo.controller;

import com.example.demo.service.StudyService;
import com.example.demo.vo.BtnResult;
import com.example.demo.vo.ChartResult;
import com.example.demo.vo.StudentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.sql.SQLException;

@Controller
public class StudyController {
    @Autowired
    StudyService studyService;

    @CrossOrigin
    @GetMapping(value = "api/study/classification/pie")
    @ResponseBody
    public ChartResult getClassByPie(@RequestParam("coursename")String courseName) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        return studyService.getClassByPie(courseName);
    }

    @CrossOrigin
    @GetMapping(value = "api/study/classification/line")
    @ResponseBody
    public ChartResult getClassByLine(@RequestParam("coursename")String courseName) throws ClassNotFoundException, SQLException, InterruptedException, IOException {
        return studyService.getClassByLine(courseName);
    }

    @CrossOrigin
    @GetMapping(value = "api/study/classification/btn")
    @ResponseBody
    public BtnResult getBtnData(@RequestParam("coursename")String courseName) throws ClassNotFoundException, SQLException, InterruptedException, IOException {
        return studyService.getBtnData(courseName);
    }

    @CrossOrigin
    @GetMapping(value = "api/study/classification/student")
    @ResponseBody
    public StudentResult getStudentList(@RequestParam("coursename")String courseName,
                                        @RequestParam("type")String type,
                                        @RequestParam("pagenum")int pagenum,
                                        @RequestParam("pagesize")int pagesize) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        // 两种情况 获得全部list 和 获得制定stuID的
        return studyService.getStudentList(courseName,type,pagenum,pagesize);
    }
}
