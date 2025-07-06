package com.example.demo.service;

import com.example.demo.dao.CourseDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.Course;
import com.example.demo.entity.User;
import com.example.demo.utils.ExcelImportUtil;
import com.example.demo.utils.PageUtil;
import com.example.demo.utils.SqlUtil;
import com.example.demo.vo.ListResult;
import com.example.demo.vo.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private UserDao userDao;

    @Mock
    private PageUtil pageUtil;

    @Mock
    private CourseDao courseDao;

    @Mock
    private SqlUtil sqlUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testImportFile_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new byte[10]);
        when(courseDao.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Result result = fileService.importFile(file, "courseA", "classA", "userA");
        assertEquals(200, result.getCode());
    }

    @Test
    void testGetFileList() {
        User mockUser = new User();
        mockUser.setCourse("course1,course2,course3");
        when(userDao.findByUsername("userA")).thenReturn(mockUser);
        List<Course> courseList = new ArrayList<>();
        courseList.add(new Course("course1"));
        courseList.add(new Course("course2"));
        courseList.add(new Course("course3"));
        when(pageUtil.startPage(anyList(), eq(1), eq(10))).thenReturn(courseList);
        var listResult = fileService.getFileList("userA", 1, 10);
        assertEquals(3, listResult.getTotal());
        assertEquals(3, listResult.getList().size());
    }

    @Test
    void testRemoveFile_Success() throws Exception {
        when(userDao.findByUsername("userA")).thenReturn(new User());
        when(sqlUtil.removeTables("course1")).thenReturn(true);
        Result result = fileService.removeFile("course1", "userA");
        assertEquals(200, result.getCode());
        verify(userDao).save(any(User.class));
    }

    @Test
    void testRemoveFile_Failure() throws Exception {
        when(userDao.findByUsername("userA")).thenReturn(new User());
        when(sqlUtil.removeTables("course1")).thenReturn(false);
        Result result = fileService.removeFile("course1", "userA");
        assertEquals(400, result.getCode());
    }
}
