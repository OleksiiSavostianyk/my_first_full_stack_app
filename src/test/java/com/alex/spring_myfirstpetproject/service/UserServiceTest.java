package com.alex.spring_myfirstpetproject.service;

import com.alex.spring_myfirstpetproject.DAO.*;
import com.alex.spring_myfirstpetproject.entities.Course;
import com.alex.spring_myfirstpetproject.entities.User;
import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;




public  User getTestUser(){
    User user = new User();
    user.setUsername("testName");
    user.setPassword("testPass");
    user.setId(1L);
    return user;
}

public Course getTestCourse(){
    Course course = new Course();
    course.setId(1L);
    course.setCourseName("testCourseName");
    return course;
}


    @Test
    void registerUser() throws Exception {
        User user = getTestUser();

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User inputUser = userService.registerUser(user);

        assertNotNull(inputUser);
        assertEquals(user.getId(), inputUser.getId());
        assertEquals(user.getUsername(), inputUser.getUsername());
        assertEquals(user.getPassword(), inputUser.getPassword());
        assertEquals(user, inputUser);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));

    }

    @Test
    void updateUser() {
        String newName =  "NewName";

        User user = getTestUser();
        user.setUsername(newName);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User inputUser = userService.updateUser(user);


        assertNotNull(inputUser);
        assertEquals(user.getId(), inputUser.getId());
        assertEquals(user.getUsername(), inputUser.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));


    }

    @Test
    void getUserById() {
    User user = getTestUser();
     Optional<User> userOptional = Optional.of(user);
    Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(userOptional);
    User inputUser = userService.getUserById(user.getId());

    assertNotNull(inputUser);
    assertEquals(user.getId(), inputUser.getId());
    Mockito.verify(userRepository, Mockito.times(1)).findById(user.getId());
    }

    @Test
    void getUserByIdExceptionCase(){

    assertThrows(UsernameNotFoundException.class, () -> userService.getUserById(123));

    }

    @Test
    void getUserByName() {
    User user = getTestUser();
    Optional<User> userOptional = Optional.of(user);
    Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(userOptional);

    User inputUser = userService.getUserByName(user.getUsername());
    assertNotNull(inputUser);
    assertEquals(user.getId(), inputUser.getId());
    assertEquals(user.getUsername(), inputUser.getUsername());
    Mockito.verify(userRepository, Mockito.times(1)).findByUsername(user.getUsername());
    }

    @Test
    void getUserByNameExceptionCase(){
    assertThrows(UsernameNotFoundException.class, () -> userService.getUserByName(""));
    }

    @Test
    void getAllUsers() {
    List<User> users = new ArrayList<>();
    users.add(getTestUser());

    Mockito.when(userRepository.findAll()).thenReturn(users);

    List<User> inputUsers = userService.getAllUsers();

    assertNotNull(inputUsers);
    assertNotNull(inputUsers.get(0));
    assertEquals(users.size(), inputUsers.size());
    Mockito.verify(userRepository, Mockito.times(1)).findAll();

    }

    @Test
    void addCourse() {
    Course course = getTestCourse();

    Mockito.when(courseRepository.save(Mockito.any(Course.class))).thenReturn(course);
    Course inputCourse = userService.addCourse(course);
    assertNotNull(inputCourse);
    assertEquals(course.getId(), inputCourse.getId());
    Mockito.verify(courseRepository, Mockito.times(1)).save(Mockito.any(Course.class));
    }

    @Test
    void getAllCourses() {
    List<Course> courses = new ArrayList<>();
    courses.add(getTestCourse());
    Mockito.when(courseRepository.findAll()).thenReturn(courses);
    List<Course> inputCourses = userService.getAllCourses();
    assertNotNull(inputCourses);
    assertNotNull(inputCourses.get(0));
    assertEquals(courses.size(), inputCourses.size());
    Mockito.verify(courseRepository, Mockito.times(1)).findAll();
    }

    @Test
    void deleteCourse() {
    Course course = getTestCourse();

    courseRepository.delete(course);

    Mockito.verify(courseRepository, Mockito.times(1)).delete(course);

    }

    @Test
    void getCourseByName() {
    Course course = getTestCourse();
    Optional<Course> courseOptional = Optional.of(course);

    Mockito.when(courseRepository.findByCourseName(Mockito.anyString())).thenReturn(courseOptional);

    Course inputCourse = userService.getCourseByName(course.getCourseName());

    assertNotNull(inputCourse);
    assertEquals(course.getId(), inputCourse.getId());
    Mockito.verify(courseRepository, Mockito.times(1)).findByCourseName(course.getCourseName());
    }
}