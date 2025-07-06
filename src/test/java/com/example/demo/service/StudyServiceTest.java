package com.example.demo.service;

import com.example.demo.utils.SqlUtil;
import com.example.demo.vo.BtnResult;
import com.example.demo.vo.ChartResult;
import com.example.demo.vo.StudentResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudyServiceTest {

    @InjectMocks
    private StudyService studyService;

    @Mock
    private SqlUtil sqlUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClass() throws Exception {
        String courseName = "course1";

        List<Map<String, String>> studentList = new ArrayList<>();
        Map<String, String> student1 = new HashMap<>();
        student1.put("name", "Alice");
        student1.put("id", "1");
        student1.put("score", "90");
        studentList.add(student1);

        StudentResult mockStudentResult = new StudentResult();
        mockStudentResult.setStudents(studentList);

        when(sqlUtil.getStudentList(courseName)).thenReturn(mockStudentResult);

        // We will mock Process and BufferedReader behavior if necessary,
        // but here just test the returned list size as basic check
        List<Map<String, String>> result = studyService.getClass(courseName);

        assertNotNull(result);
        assertEquals(studentList.size(), result.size());
    }

    @Test
    void testGetClassByPie() throws Exception {
        String courseName = "course1";

        List<Map<String, String>> studentList = new ArrayList<>();
        Map<String, String> s1 = new HashMap<>();
        s1.put("类别", "0");
        Map<String, String> s2 = new HashMap<>();
        s2.put("类别", "1");
        Map<String, String> s3 = new HashMap<>();
        s3.put("类别", "2");
        Map<String, String> s4 = new HashMap<>();
        s4.put("类别", "3");
        studentList.add(s1);
        studentList.add(s2);
        studentList.add(s3);
        studentList.add(s4);

        StudyService spyService = Mockito.spy(studyService);
        Mockito.doReturn(studentList).when(spyService).getClass(courseName);

        ChartResult result = spyService.getClassByPie(courseName);

        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
    }

    @Test
    void testGetClassData() throws Exception {
        String courseName = "course1";

        List<Map<String, String>> studentList = new ArrayList<>();

        Map<String, String> s1 = new HashMap<>();
        s1.put("类别", "0");
        s1.put("col3", "10");
        s1.put("col4", "20");
        s1.put("col5", "30");
        s1.put("col7", "40");
        s1.put("col8", "50");
        s1.put("col9", "60");

        Map<String, String> s2 = new HashMap<>();
        s2.put("类别", "1");
        s2.put("col3", "15");
        s2.put("col4", "25");
        s2.put("col5", "35");
        s2.put("col7", "45");
        s2.put("col8", "55");
        s2.put("col9", "65");

        studentList.add(s1);
        studentList.add(s2);

        StudyService spyService = Mockito.spy(studyService);
        Mockito.doReturn(studentList).when(spyService).getClass(courseName);

        String[] colNames = {"col0", "col1", "col2", "col3", "col4", "col5", "col6", "col7", "col8", "col9"};
        when(sqlUtil.getColName(courseName)).thenReturn(colNames);

        List<int[]> list = spyService.getClassData(courseName);

        assertNotNull(list);
        assertEquals(7, list.size());
    }

    @Test
    void testGetClassByLine() throws Exception {
        String courseName = "course1";

        StudyService spyService = Mockito.spy(studyService);
        List<int[]> mockList = new ArrayList<>();
        mockList.add(new int[]{10, 20, 30, 40});
        mockList.add(new int[]{15, 25, 35, 45});
        mockList.add(new int[]{5, 10, 15, 20});
        mockList.add(new int[]{7, 14, 21, 28});
        mockList.add(new int[]{8, 16, 24, 32});
        mockList.add(new int[]{9, 18, 27, 36});
        mockList.add(new int[]{1, 1, 1, 1});

        Mockito.doReturn(mockList).when(spyService).getClassData(courseName);

        ChartResult result = spyService.getClassByLine(courseName);

        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
    }

    @Test
    void testGetBtnData() throws Exception {
        String courseName = "course1";

        StudyService spyService = Mockito.spy(studyService);

        List<int[]> mockList = new ArrayList<>();
        mockList.add(new int[]{10, 20, 30, 40});
        mockList.add(new int[]{15, 25, 35, 45});
        mockList.add(new int[]{5, 10, 15, 20});
        mockList.add(new int[]{7, 14, 21, 28});
        mockList.add(new int[]{8, 16, 24, 32});
        mockList.add(new int[]{9, 18, 27, 36});
        mockList.add(new int[]{1, 2, 3, 4});

        Mockito.doReturn(mockList).when(spyService).getClassData(courseName);

        BtnResult result = spyService.getBtnData(courseName);

        assertEquals("10", result.getBtn2());
        assertEquals("20", String.valueOf(result.getBtn3().charAt(0)));// Actually returns string, adjust if needed
        assertEquals("4", result.getBtn5());
        assertEquals(200, result.getCode());
    }

    @Test
    void testGetStudentList_Type4() throws Exception {
        String courseName = "course1";
        String type = "4";

        StudentResult mockResult = new StudentResult();
        mockResult.setCode(200);

        when(sqlUtil.getStudentList(courseName)).thenReturn(mockResult);

        StudentResult result = studyService.getStudentList(courseName, type, 1, 10);

        assertEquals(200, result.getCode());
        verify(sqlUtil).getStudentList(courseName);
    }

    @Test
    void testGetStudentList_OtherType() throws Exception {
        String courseName = "course1";
        String type = "1";

        List<Map<String, String>> studentList = new ArrayList<>();
        Map<String, String> student = new HashMap<>();
        student.put("类别", "1");
        studentList.add(student);

        StudyService spyService = Mockito.spy(studyService);
        Mockito.doReturn(studentList).when(spyService).getClass(courseName);

        StudentResult result = spyService.getStudentList(courseName, type, 1, 10);

        assertEquals(200, result.getCode());
        assertEquals(1, result.getStudents().size());
    }
}
