package com.alex.spring_myfirstpetproject.service;

import com.alex.spring_myfirstpetproject.DAO.CourseRepository;
import com.alex.spring_myfirstpetproject.DAO.UserRepository;
import com.alex.spring_myfirstpetproject.controller.WebPasswordEncoder;
import com.alex.spring_myfirstpetproject.entities.Course;
import com.alex.spring_myfirstpetproject.entities.User;
import com.alex.spring_myfirstpetproject.exception.handler.EmailAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {


    private BCryptPasswordEncoder passwordEncoder;

    private CourseRepository courseRepository;

    private UserRepository userRepository;

    @Autowired
    public UserService(CourseRepository courseRepository, UserRepository userRepository,BCryptPasswordEncoder passwordEncoder) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User registerUser(User user) throws Exception {
        if (user != null) {

            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new EmailAlreadyExistException("Email: " + user.getEmail() + " already exist");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            return userRepository.save(user);
        }else {
            throw new UsernameNotFoundException("User is null");
        }
    }

    @Override
    public User updateUser(User user) {
       return userRepository.save(user);
    }


    @Override
    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    @Override
    public User getUserByName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    @Override
    public User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }




    @Override
    public User loginUser(User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());

        if (userOptional.isPresent()) {
            User userFromDb = userOptional.get();
            if (WebPasswordEncoder.matches(user.getPassword(), userFromDb.getPassword())) {
                return userFromDb;
            }
            throw new UsernameNotFoundException("Invalid  password");
        }
        throw  new UsernameNotFoundException("User not found");
    }





    @Override
    public Course addCourse(Course course) {
      return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public void deleteCourse(Course course) {
         courseRepository.delete(course);
    }

    @Override
    public Course getCourseByName(String courseName) {
        return courseRepository.findByCourseName(courseName)
                .orElseThrow(() -> new UsernameNotFoundException("Course not found"));
    }

    @Override
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course getCourseById(long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Course not found"));
    }

    @Override
    public boolean addStudentToCourse(long courseId, String studentName) {
       Course course = getCourseById(courseId);
       User user = getUserByName(studentName);

       if (course.getStudents().contains(user)) {
           return false;
       }

       course.addStudent(user);

       Course upatedCourse = courseRepository.save(course);

       Optional<User> userfromCourse = upatedCourse.getStudent(studentName);

        return userfromCourse.map(value -> value.equals(user)).orElse(false);


    }


}
