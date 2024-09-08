package com.alex.spring_myfirstpetproject.DAO;

import com.alex.spring_myfirstpetproject.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByCourseName(String courseName);
    Optional<Course> findById(long Id);
}
