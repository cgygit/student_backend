package com.example.demo.service;

import com.example.demo.dao.CourseDao;
import com.example.demo.entity.Course;
import com.example.demo.utils.SqlUtil;
import com.example.demo.vo.ChartResult;
import com.example.demo.vo.StatListResult;
import com.example.demo.vo.OneDataResult;
import com.example.demo.vo.TwoDataResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StatisticsServiceTest {

    @InjectMocks
    private StatisticsService statisticsService;

    @Mock
    private CourseDao courseDao;

    @Mock
    private SqlUtil sqlUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClassLine() throws Exception {
        Course mockCourse = new Course();
        mockCourse.setLesson("course_table1,course_table2,");

        when(courseDao.findByName(anyString())).thenReturn(mockCourse);

        List<List<Integer>> mockData = Arrays.asList(
            Arrays.asList(1,2,3,4),
            Arrays.asList(5,6,7,8)
        );
        when(sqlUtil.getClassDataByTable(anyString(), anyList())).thenReturn(mockData);

        ChartResult result = statisticsService.getClassLine("course1");

        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
    }

    @Test
    void testGetClassBar() throws Exception {
        Course mockCourse = new Course();
        mockCourse.setLesson("course_table1,course_table2,");

        when(courseDao.findByName(anyString())).thenReturn(mockCourse);

        List<List<Integer>> mockBarData = Arrays.asList(
            Arrays.asList(10, 20),
            Arrays.asList(30, 40)
        );
        when(sqlUtil.getBarDataOfClass(anyList())).thenReturn(mockBarData);

        TwoDataResult result = statisticsService.getClassBar("course1");

        assertEquals(200, result.getCode());
        assertEquals(mockBarData.get(0), result.getData1());
        assertEquals(mockBarData.get(1), result.getData2());
    }

    @Test
    void testGetBeforeClassLine() throws Exception {
        Course mockCourse = new Course();
        mockCourse.setReadiness("ready_table1,ready_table2,");

        when(courseDao.findByName(anyString())).thenReturn(mockCourse);

        List<List<Integer>> mockLineData = Arrays.asList(
            Arrays.asList(1,2,3),
            Arrays.asList(4,5,6)
        );
        when(sqlUtil.getLineDataOfBeforeClass(anyList())).thenReturn(mockLineData);

        TwoDataResult result = statisticsService.getBeforeClassLine("course1");

        assertEquals(200, result.getCode());
        assertEquals(mockLineData.get(0), result.getData1());
        assertEquals(mockLineData.get(1), result.getData2());
        assertNotNull(result.getxAxis());
    }

    @Test
    void testGetBeforeClassBar() throws Exception {
        Course mockCourse = new Course();
        mockCourse.setReadiness("ready_table1,ready_table2,");

        when(courseDao.findByName(anyString())).thenReturn(mockCourse);

        List<List<Integer>> mockBarData = Arrays.asList(
            Arrays.asList(10, 20),
            Arrays.asList(30, 40)
        );
        when(sqlUtil.getBarDataOfBeforeClass(anyList())).thenReturn(mockBarData);

        TwoDataResult result = statisticsService.getBeforeClassBar("course1");

        assertEquals(200, result.getCode());
        assertEquals(mockBarData.get(0), result.getData1());
        assertEquals(mockBarData.get(1), result.getData2());
        assertNull(result.getxAxis());
    }

    @Test
    void testGetExamLine() throws Exception {
        Course mockCourse = new Course();
        mockCourse.setExam("exam_table1,exam_table2,");

        when(courseDao.findByName(anyString())).thenReturn(mockCourse);

        List<Integer> mockLineData = Arrays.asList(100, 200, 300);
        when(sqlUtil.getLineDataOfExam(anyList())).thenReturn(mockLineData);

        OneDataResult result = statisticsService.getExamLine("course1");

        assertEquals(200, result.getCode());
        assertEquals(mockLineData, result.getData());
        assertNotNull(result.getxAxis());
    }

    @Test
    void testGetClassList() throws Exception {
        Course mockCourse = new Course();
        mockCourse.setLesson("lesson1,lesson2,");
        when(courseDao.findByName(anyString())).thenReturn(mockCourse);

        List<List<Map<String,String>>> mockList = new ArrayList<>();
        List<Map<String,String>> students = new ArrayList<>();
        Map<String,String> student1 = new HashMap<>();
        student1.put("学号", "123");
        student1.put("姓名", "Alice");
        student1.put("签到方式", "present");
        student1.put("投稿次数", "1");
        student1.put("弹幕次数", "2");
        student1.put("答题得分", "90");
        students.add(student1);
        mockList.add(students);
        mockList.add(new ArrayList<>());
        when(sqlUtil.getClassList(anyList())).thenReturn(mockList);

        StatListResult result = statisticsService.getClassList("course1", 1, "123", "Alice", "present", "2", "3", "100", 1, 10);

        assertEquals(200, result.getCode());
        assertEquals(1, result.getStudents().size());
    }

    // 你可以继续写其他方法的测试用例
}
