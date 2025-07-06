package com.example.demo.controller;

import com.example.demo.service.StudentService;
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

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    void testGetStudentList() throws Exception {
        StudentResult mockResult = new StudentResult();

        when(studentService.getStudentList(anyString(), anyString(), anyInt(), anyInt()))
            .thenReturn(mockResult);

        mockMvc.perform(get("/api/student")
                .param("coursename", "Math")
                .param("stuID", "123")
                .param("pagenum", "1")
                .param("pagesize", "10"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }
}
