package com.alex.spring_myfirstpetproject.controller;

import com.alex.spring_myfirstpetproject.entities.Course;
import com.alex.spring_myfirstpetproject.entities.User;
import com.alex.spring_myfirstpetproject.service.UserService;
import com.alex.spring_myfirstpetproject.service.UserServiceInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LearningManagementSystemRestControllerTest {
    private final String userRole = "ROLE_USER";
    private  final String adminRole = "ROLE_ADMIN";


    @InjectMocks
    private LearningManagementSystemRestController controller;


    @Mock
    private UserService userService;


    @Mock
    private Authentication authentication;


    private User getUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("test@test.com");
        user.addRole(userRole);
        return user;
    }

    private Course getCourse(){
        Course course = new Course();
        course.setId(1L);
        course.setCourseName("testCourse");
        return course;
    }


    private Collection<GrantedAuthority> getAuthorities(String role){
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        Collection<GrantedAuthority> authorities = Collections.singleton(authority);
        return authorities;
    }


    @Test
    void registerUser() throws Exception {


        User user = getUser();
        user.addRole("NON_CORRECT_ROLE");

        when(userService.registerUser(user)).thenReturn(user);

        ResponseEntity<User> response = controller.registerUser(user);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user,response.getBody());
        assertNotNull(response.getBody());
        Mockito.verify(userService).registerUser(user);
        assertEquals(userRole,response.getBody().getRole());

    }


    @Test
    void registerUserException() throws Exception {
        User user = null;

        lenient().when(userService.registerUser(user)).thenReturn(user);
        ResponseEntity<User> response = controller.registerUser(user);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(user,response.getBody());


    }

    @Test
    void getUserRoles() {

        List<String> roles = Arrays.asList(userRole);

       when(authentication.isAuthenticated()).thenReturn(true);


        Collection<GrantedAuthority> authorities = getAuthorities(userRole);

       when(authentication.getAuthorities()).thenReturn((Collection)authorities);
        ResponseEntity<List<String>> response = controller.getUserRoles(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roles,response.getBody());
        verify(authentication).getAuthorities();


    }

    @Test
    void getUserRolesNonAuthentificatedUser()  {
        when(authentication.isAuthenticated()).thenReturn(false);
        ResponseEntity<List<String>> response = controller.getUserRoles(authentication);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getCourses() {

        List<Course> courses = Arrays.asList();

         Course course = getCourse();
        when(authentication.isAuthenticated()).thenReturn(true);
        when(userService.getAllCourses()).thenReturn(courses);

        ResponseEntity<List<Course>> response = controller.getCourses(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courses,response.getBody());
        verify(authentication).isAuthenticated();
        verify(userService).getAllCourses();



    }

    @Test
    void getCoursesNonAuthentificatedUser()  {
        when(authentication.isAuthenticated()).thenReturn(false);
        ResponseEntity<List<Course>> response = controller.getCourses(authentication);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void addNewCourse() {
        Course course = getCourse();

        Collection<GrantedAuthority> authorities = getAuthorities(adminRole);

        when(authentication.getAuthorities()).thenReturn((Collection)authorities);
        when(userService.addCourse(course)).thenReturn(course);

        ResponseEntity<Course> courseResponseEntity = controller.addNewCourse(course,authentication);

        assertEquals(HttpStatus.OK, courseResponseEntity.getStatusCode());
        assertEquals(course,courseResponseEntity.getBody());
        verify(authentication).getAuthorities();
        verify(userService).addCourse(course);


    }

    @Test
    void addNewCourseNonAdminUser()  {

        Collection<GrantedAuthority> authorities = getAuthorities(userRole);
        when(authentication.getAuthorities()).thenReturn((Collection)authorities);
        ResponseEntity<Course> courseResponseEntity = controller.addNewCourse(getCourse(),authentication);
        assertEquals(HttpStatus.UNAUTHORIZED, courseResponseEntity.getStatusCode());

    }

    @Test
    void getCourseDetails() {

        Course course = getCourse();
        String courseId = String.valueOf(course.getId());


        when(authentication.isAuthenticated()).thenReturn(true);


        when(userService.getCourseById(course.getId())).thenReturn(course);
        ResponseEntity<Course> responseEntity = controller.getCourseDetails(courseId,authentication);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(course,responseEntity.getBody());
        verify(authentication).isAuthenticated();
        verify(userService).getCourseById(course.getId());

    }

    @Test
    void getCourseDetailsNonAdminUser()  {
        when(authentication.isAuthenticated()).thenReturn(false);
        ResponseEntity<Course> courseResponseEntity = controller.getCourseDetails("1",authentication);
        assertEquals(HttpStatus.UNAUTHORIZED, courseResponseEntity.getStatusCode());
    }


    @Test
    void testUpdateCourse() {
         Course course = getCourse();

         when(authentication.isAuthenticated()).thenReturn(true);
         when(authentication.getAuthorities()).thenReturn((Collection)getAuthorities(adminRole));
         when(userService.updateCourse(course)).thenReturn(course);

          controller.updateCourse("1",course,authentication);

          verify(authentication).getAuthorities();
          verify(userService).updateCourse(course);
          verify(authentication).isAuthenticated();
    }
}