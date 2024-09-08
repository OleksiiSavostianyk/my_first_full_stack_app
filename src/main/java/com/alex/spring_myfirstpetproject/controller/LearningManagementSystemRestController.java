package com.alex.spring_myfirstpetproject.controller;


import com.alex.spring_myfirstpetproject.entities.Course;
import com.alex.spring_myfirstpetproject.entities.Roles;
import com.alex.spring_myfirstpetproject.entities.User;
import com.alex.spring_myfirstpetproject.service.UserService;
import com.alex.spring_myfirstpetproject.service.UserServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import jdk.jfr.MemoryAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LearningManagementSystemRestController {

    private static final String adminRole = "ROLE_ADMIN";
    private static final String studentRole = "ROLE_STUDENT";


    private UserServiceInterface userService;

    @Autowired
    public LearningManagementSystemRestController(UserService userService) {
        this.userService = userService;
    }






    @PostMapping("/registration")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            user.addRole(String.valueOf(Roles.STUDENT));
        User registeredUser = userService.registerUser(user);

         return ResponseEntity.ok(registeredUser);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping("/user/roles")
    public ResponseEntity<List<String>> getUserRoles(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Получение ролей пользователя из Authentication
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }


    @GetMapping("/get/all/courses")
    public ResponseEntity<List<Course>> getCourses(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Course> courses = userService.getAllCourses();

        return ResponseEntity.ok(courses);
    }


    @PostMapping("/admin/add/new/course")
    public ResponseEntity<Course> addNewCourse(@RequestBody Course course,Authentication authentication ) {

        if (!RoleCheck.checkAccess(authentication, adminRole)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

            if (course.getCourseName() == null || course.getCourseName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            try {
                userService.addCourse(course);
                return ResponseEntity.ok(course);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @GetMapping("/course/{courseId}/details")
    public ResponseEntity<Course> getCourseDetails(@PathVariable String courseId,Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        Course course = userService.getCourseById(Long.parseLong(courseId));
        return  ResponseEntity.ok(course);

    }



    @PutMapping("/admin/update/course/{courseId}")
    public ResponseEntity<Void> updateCourse(@PathVariable String courseId,@RequestBody Course course,Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!RoleCheck.checkAccess(authentication,adminRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

       Course updatedCourse =  userService.updateCourse(course);
        if (updatedCourse == null) { return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); }
        return ResponseEntity.ok().build();
    }













}
