package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.service.ScoreService;
import com.example.demo.vo.ScoreResult;
import com.example.demo.vo.StudentResult;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ScoreController {
    @Autowired
    ScoreService scoreService;

    @CrossOrigin
    @GetMapping(value = "api/score")
    @ResponseBody
    public ScoreResult getScoreList(@RequestParam("coursename")String courseName,
                                    @RequestParam("stuID")String stuID,
                                    @RequestParam("setting")String str,
                                    @RequestParam("pagenum")int pagenum,
                                    @RequestParam("pagesize")int pagesize) throws SQLException, ClassNotFoundException {
        //Map setting = (Map) JSON.parse(str);
        Map setting = JSON.parseObject(str);
        //System.out.println(setting);
        return scoreService.getScoreList(courseName, stuID, setting, pagenum, pagesize);
    }

    @CrossOrigin
    @GetMapping(value = "api/score/filter")
    @ResponseBody
    public ScoreResult getScoreListByFilter(@RequestParam("point") String point,
                                            @RequestParam("page") String page,
                                            @RequestParam("attendance") String attendance,
                                            @RequestParam("barrage") String barrage,
                                            @RequestParam("submission") String submission,
                                            @RequestParam("announcement") String announcement,
                                            @RequestParam("average") String average,
                                            @RequestParam("coursename")String courseName,
                                            @RequestParam("stuID")String stuID,
                                            @RequestParam("setting")String str,
                                            @RequestParam("pagenum")int pagenum,
                                            @RequestParam("pagesize")int pagesize) throws SQLException, ClassNotFoundException {
        Map setting = JSON.parseObject(str);
        return scoreService.getScoreListByFilter(point, page, attendance, barrage, submission, announcement,average,courseName, stuID, setting, pagenum, pagesize);
    }
}
