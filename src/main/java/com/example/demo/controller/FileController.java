package com.example.demo.controller;

import com.example.demo.service.FileService;
import com.example.demo.vo.ListResult;
import com.example.demo.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

@Controller
public class FileController {
    @Autowired
    FileService fileService;

    @CrossOrigin
    @PostMapping("api/file")
    @ResponseBody
    public Result importFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("coursename") String coursename,
                             @RequestParam("classname") String classname,
                             @RequestParam("username") String username) throws Exception {
        return fileService.importFile(file, coursename, classname, username);
    }

    @CrossOrigin
    @GetMapping("api/file")
    @ResponseBody
    public ListResult getFileList(@RequestParam("username") String username,
                                  @RequestParam("pagenum")int pagenum,
                                  @RequestParam("pagesize")int pagesize) {
        return fileService.getFileList(username, pagenum, pagesize);
    }

    @CrossOrigin
    @PostMapping("api/file/remove")
    @ResponseBody
    public Result removeFile(@RequestParam("name") String fileName, @RequestParam("username") String username) throws SQLException, ClassNotFoundException {
        return fileService.removeFile(fileName, username);
    }

    @CrossOrigin
    @PostMapping("api/file/update")
    @ResponseBody
    public Result updateFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("course") String course,
                             @RequestParam("type") String type) throws Exception {
        return fileService.updateFile(file, course, type);
    }
}
