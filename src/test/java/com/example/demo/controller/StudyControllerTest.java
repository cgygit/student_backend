package com.example.demo.controller;

import com.example.demo.service.StudyService;
import com.example.demo.vo.BtnResult;
import com.example.demo.vo.ChartResult;
import com.example.demo.vo.StudentResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudyController.class)
public class StudyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudyService studyService;

    @Test
    void testGetClassByPie() throws Exception {
        ChartResult mockResult = new ChartResult();
        when(studyService.getClassByPie(anyString())).thenReturn(mockResult);

        mockMvc.perform(get("/api/study/classification/pie")
                .param("coursename", "Math"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetClassByLine() throws Exception {
        ChartResult mockResult = new ChartResult();
        when(studyService.getClassByLine(anyString())).thenReturn(mockResult);

        mockMvc.perform(get("/api/study/classification/line")
                .param("coursename", "Math"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetBtnData() throws Exception {
        BtnResult mockResult = new BtnResult();
        when(studyService.getBtnData(anyString())).thenReturn(mockResult);

        mockMvc.perform(get("/api/study/classification/btn")
                .param("coursename", "Math"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetStudentList() throws Exception {
        StudentResult mockResult = new StudentResult();
        when(studyService.getStudentList(anyString(), anyString(), anyInt(), anyInt())).thenReturn(mockResult);

        mockMvc.perform(get("/api/study/classification/student")
                .param("coursename", "Math")
                .param("type", "all")
                .param("pagenum", "1")
                .param("pagesize", "10"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }
}
