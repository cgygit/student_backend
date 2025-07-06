package com.example.demo.controller;

import com.example.demo.service.ScoreService;
import com.example.demo.vo.ScoreResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScoreController.class)
public class ScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScoreService scoreService;

    @Test
    void testGetScoreList() throws Exception {
        ScoreResult mockResult = new ScoreResult();

        when(scoreService.getScoreList(anyString(), anyString(), anyMap(), anyInt(), anyInt()))
            .thenReturn(mockResult);

        mockMvc.perform(get("/api/score")
                .param("coursename", "Math")
                .param("stuID", "123")
                .param("setting", "{}")
                .param("pagenum", "1")
                .param("pagesize", "10"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetScoreListByFilter() throws Exception {
        ScoreResult mockResult = new ScoreResult();

        when(scoreService.getScoreListByFilter(
                anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyMap(), anyInt(), anyInt()))
            .thenReturn(mockResult);

        mockMvc.perform(get("/api/score/filter")
                .param("point", "90")
                .param("page", "1")
                .param("attendance", "95")
                .param("barrage", "5")
                .param("submission", "100")
                .param("announcement", "3")
                .param("average", "85")
                .param("coursename", "Math")
                .param("stuID", "123")
                .param("setting", "{}")
                .param("pagenum", "1")
                .param("pagesize", "10"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }
}
