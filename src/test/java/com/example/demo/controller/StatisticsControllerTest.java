package com.example.demo.controller;

import com.example.demo.service.StatisticsService;
import com.example.demo.vo.ChartResult;
import com.example.demo.vo.StatListResult;
import com.example.demo.vo.OneDataResult;
import com.example.demo.vo.TwoDataResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;

    @Test
    void testGetClassLine() throws Exception {
        ChartResult mockResult = new ChartResult();
        when(statisticsService.getClassLine(anyString())).thenReturn(mockResult);

        mockMvc.perform(get("/api/statistics/onClass/line")
                .param("coursename", "Math"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetClassBar() throws Exception {
        TwoDataResult mockResult = new TwoDataResult();
        when(statisticsService.getClassBar(anyString())).thenReturn(mockResult);

        mockMvc.perform(get("/api/statistics/onClass/bar")
                .param("coursename", "Math"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetClassList() throws Exception {
        StatListResult mockResult = new StatListResult();
        when(statisticsService.getClassList(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(mockResult);

        mockMvc.perform(get("/api/statistics/onClass/list")
                .param("coursename", "Math")
                .param("num", "1")
                .param("stuID", "123")
                .param("stuName", "Alice")
                .param("attendType", "present")
                .param("submission", "yes")
                .param("barrage", "none")
                .param("point", "90")
                .param("pagenum", "1")
                .param("pagesize", "10"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetBeforeClassLine() throws Exception {
        TwoDataResult mockResult = new TwoDataResult();
        when(statisticsService.getBeforeClassLine(anyString())).thenReturn(mockResult);

        mockMvc.perform(get("/api/statistics/beforeClass/line")
                .param("coursename", "Math"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetBeforeClassBar() throws Exception {
        TwoDataResult mockResult = new TwoDataResult();
        when(statisticsService.getBeforeClassBar(anyString())).thenReturn(mockResult);

        mockMvc.perform(get("/api/statistics/beforeClass/bar")
                .param("coursename", "Math"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetBeforeClassList() throws Exception {
        StatListResult mockResult = new StatListResult();
        when(statisticsService.getBeforeClassList(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(mockResult);

        mockMvc.perform(get("/api/statistics/beforeClass/list")
                .param("coursename", "Math")
                .param("num", "1")
                .param("stuID", "123")
                .param("stuName", "Alice")
                .param("page", "1")
                .param("totalTime", "60")
                .param("endTime", "2025-07-01")
                .param("point", "90")
                .param("pagenum", "1")
                .param("pagesize", "10"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetExamLine() throws Exception {
        OneDataResult mockResult = new OneDataResult();
        when(statisticsService.getExamLine(anyString())).thenReturn(mockResult);

        mockMvc.perform(get("/api/statistics/exam/line")
                .param("coursename", "Math"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetExamClassList() throws Exception {
        StatListResult mockResult = new StatListResult();
        when(statisticsService.getExamClassList(anyString(), anyInt(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(mockResult);

        mockMvc.perform(get("/api/statistics/exam/list")
                .param("coursename", "Math")
                .param("num", "1")
                .param("stuID", "123")
                .param("stuName", "Alice")
                .param("point", "90")
                .param("totalTime", "60")
                .param("endTime", "2025-07-01")
                .param("pagenum", "1")
                .param("pagesize", "10"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }
}
