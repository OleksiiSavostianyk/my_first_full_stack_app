package com.alex.spring_myfirstpetproject.service;


import com.alex.spring_myfirstpetproject.entities.Course;
import com.alex.spring_myfirstpetproject.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {

    public User registerUser(User user) throws Exception;
    public User updateUser(User user);
    public User getUserById(long id);
    public User getUserByName(String username);
    public List<User> getAllUsers();
    public User getUserByEmail(String email);
    public User loginUser(User user);



    public Course addCourse(Course course);
    public List<Course> getAllCourses();
    public void deleteCourse(Course course);
    public Course getCourseByName(String courseName);
    public Course updateCourse(Course course);
    public Course getCourseById(long id);
    public boolean addStudentToCourse(long courseId, String studentName);





}
