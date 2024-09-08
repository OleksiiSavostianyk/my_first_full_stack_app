package com.alex.spring_myfirstpetproject.controller;

import com.alex.spring_myfirstpetproject.DAO.CourseRepository;
import com.alex.spring_myfirstpetproject.DAO.UserRepository;
import com.alex.spring_myfirstpetproject.entities.Course;
import com.alex.spring_myfirstpetproject.entities.Roles;
import com.alex.spring_myfirstpetproject.entities.User;
import com.alex.spring_myfirstpetproject.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller()
@RequestMapping("/api/v2")
public class LearningManagementSystemController {

    private static final String adminRole = "ROLE_ADMIN";
    private static final String studentRole = "ROLE_STUDENT";


    private UserServiceInterface userService;

    @Autowired
   public LearningManagementSystemController(UserServiceInterface userService) {
        this.userService = userService;
    }


    public String authenticateAccess(Authentication authentication,String textOrHtml,String role) {
        if (!RoleCheck.checkAccess(authentication, role)) {
            return "redirect:/menu.html";
        }
        return textOrHtml;
    }



    @RequestMapping("/admin/course/add")
    public String registrationNewCurse(Authentication authentication) {
        return authenticateAccess(authentication,"addNewCourse",adminRole );
    }


    @RequestMapping("/menu/view")
    public String menu(Authentication authentication) {
        System.out.println(authentication.getAuthorities());//TODO
        return "redirect:/menu.html";
    }
    @RequestMapping("/logout")
    public void logout() {}



    @RequestMapping("/start")
    public String start(Authentication authentication) {
        System.out.println(authentication.getAuthorities());//TODO

        return "redirect:/startPage.html";
    }

    @RequestMapping("/view/all/courses")
    public String viewAllCourses(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/api/v2/menu/view";
        }

        return "viewAllCourses";
    }

    @RequestMapping("/courseRegister/{courseId}")
    public String registerOnCourse(@PathVariable String courseId, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/api/v2/menu/view";
        }
        if (!RoleCheck.checkAccess(authentication, studentRole)) {
            return "redirect:/api/v2/view/all/courses";
        }
      boolean result =  userService.addStudentToCourse(Long.parseLong(courseId),authentication.getName());




        return authenticateAccess(authentication,"redirect:/api/v2/view/all/courses",studentRole);
    }

    @RequestMapping("/course/{courseId}/check")
    public String details(Authentication authentication, @PathVariable String courseId, Model model) {
        Course course = userService.getCourseById(Long.parseLong(courseId));
        if (course == null) {
            return "redirect:/api/v2/menu/view";
        }
        model.addAttribute("courseId", courseId);  // Добавляем ID курса в модель
        return "courseDetail";  // Возвращаем имя шаблона HTML-страницы
    }


    @RequestMapping("/admin/course/{courseId}/edit")
    public String editCourse(Authentication authentication,@PathVariable String courseId, Model model) {
        Course course = userService.getCourseById(Long.parseLong(courseId));
        if (course == null) { return "redirect:/api/v2/view/all/courses";}
        model.addAttribute("courseId", courseId);
       return authenticateAccess(authentication,"editCourse",adminRole);
    }

    @RequestMapping("/admin/view/all/courses")
    public String viewAllCoursesAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/api/v2/menu/view";
        }

        return authenticateAccess(authentication,"viewAllCoursesAdmin",adminRole);
    }


    @RequestMapping("/admin/delete/course/{courseId}")

    public String deleteCourse(@PathVariable String courseId, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/api/v2/menu/view";
        }

        if (!RoleCheck.checkAccess(authentication, adminRole)) {
            return "redirect:/api/v2/menu/view";
        }

        Course course = userService.getCourseById(Long.parseLong(courseId));

        System.out.println("deleteCourse 3 " + courseId);


        if (course == null) { return "redirect:/api/v2/menu/view";}
        System.out.println(course);
         userService.deleteCourse(course);


        return "redirect:/api/v2/admin/view/all/courses";

    }






}
