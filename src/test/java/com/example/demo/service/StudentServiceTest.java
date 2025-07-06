package com.example.demo.service;

import com.example.demo.utils.SqlUtil;
import com.example.demo.vo.StudentResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private SqlUtil sqlUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStudentList_EmptyStuID_ReturnsAll() throws Exception {
        String courseName = "course1";
        String stuID = "";

        StudentResult mockResult = new StudentResult();
        mockResult.setCode(200);

        when(sqlUtil.getStudentList(courseName)).thenReturn(mockResult);

        StudentResult result = studentService.getStudentList(courseName, stuID, 1, 10);

        assertEquals(mockResult, result);
        verify(sqlUtil).getStudentList(courseName);
        verify(sqlUtil, never()).getStudentByID(anyString(), anyString());
    }

    @Test
    void testGetStudentList_NonEmptyStuID_ReturnsByID() throws Exception {
        String courseName = "course1";
        String stuID = "123";

        StudentResult mockResult = new StudentResult();
        mockResult.setCode(200);

        when(sqlUtil.getStudentByID(courseName, stuID)).thenReturn(mockResult);

        StudentResult result = studentService.getStudentList(courseName, stuID, 1, 10);

        assertEquals(mockResult, result);
        verify(sqlUtil).getStudentByID(courseName, stuID);
        verify(sqlUtil, never()).getStudentList(anyString());
    }
}
