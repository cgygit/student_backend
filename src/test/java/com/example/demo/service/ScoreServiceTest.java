package com.example.demo.service;

import com.example.demo.utils.SqlUtil;
import com.example.demo.vo.ScoreResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScoreServiceTest {

    @InjectMocks
    private ScoreService scoreService;

    @Mock
    private SqlUtil sqlUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetScoreList_EmptyStuID_ReturnsList() throws Exception {
        Map<Object,Object> setting = new HashMap<>();
        String courseName = "course1";
        String stuID = "";

        ScoreResult mockResult = new ScoreResult();
        mockResult.setCode(200);
        mockResult.setScores(Collections.emptyList());

        when(sqlUtil.getScoreList(courseName, setting)).thenReturn(mockResult);

        ScoreResult result = scoreService.getScoreList(courseName, stuID, setting, 1, 10);

        assertEquals(mockResult, result);
        verify(sqlUtil).getScoreList(courseName, setting);
        verify(sqlUtil, never()).getScoreById(anyString(), anyMap(), anyString());
    }

    @Test
    void testGetScoreList_NonEmptyStuID_ReturnsById() throws Exception {
        Map<Object,Object> setting = new HashMap<>();
        String courseName = "course1";
        String stuID = "123";

        ScoreResult mockResult = new ScoreResult();
        mockResult.setCode(200);

        when(sqlUtil.getScoreById(courseName, setting, stuID)).thenReturn(mockResult);

        ScoreResult result = scoreService.getScoreList(courseName, stuID, setting, 1, 10);

        assertEquals(mockResult, result);
        verify(sqlUtil).getScoreById(courseName, setting, stuID);
        verify(sqlUtil, never()).getScoreList(anyString(), anyMap());
    }

    @Test
    void testGetScoreListByFilter_FiltersApplied() throws Exception {
        String courseName = "course1";
        String stuID = "";
        Map<Object,Object> setting = new HashMap<>();

        ScoreResult allScoresResult = new ScoreResult();
        List<Map<String, String>> scoreList = new ArrayList<>();

        Map<String, String> score1 = new HashMap<>();
        score1.put("col3", "50");
        score1.put("col4", "2");
        score1.put("col5", "3");
        score1.put("col7", "1");
        score1.put("col8", "1");
        score1.put("col9", "1");
        score1.put("平时成绩", "80");
        scoreList.add(score1);

        Map<String, String> score2 = new HashMap<>();
        score2.put("col3", "90");
        score2.put("col4", "5");
        score2.put("col5", "0");
        score2.put("col7", "0");
        score2.put("col8", "0");
        score2.put("col9", "0");
        score2.put("平时成绩", "60");
        scoreList.add(score2);

        allScoresResult.setScores(scoreList);
        allScoresResult.setCode(200);

        when(scoreService.getScoreList(courseName, stuID, setting, 1, 10)).thenReturn(allScoresResult);

        String[] colNames = {"col0", "col1", "col2", "col3", "col4", "col5", "col6", "col7", "col8", "col9"};
        when(sqlUtil.getColName(courseName)).thenReturn(colNames);
        when(sqlUtil.getTotalAttendance(courseName)).thenReturn(5);

        ScoreResult filteredResult = scoreService.getScoreListByFilter(
                "80", "3", "2", "2", "2", "2", "70",
                courseName, stuID, setting, 1, 10);

        assertEquals(200, filteredResult.getCode());
        assertNotNull(filteredResult.getScores());

        for (Map<String, String> score : filteredResult.getScores()) {
            int point = Integer.parseInt(score.get(colNames[3]));
            assertTrue(point < 80);
        }
    }
}
