package com.portal.repository;

import com.portal.model.Course;
import com.portal.model.CourseTeacher;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseTeacherRepository extends JpaRepository<CourseTeacher, Long> {
    // Custom methods for `CourseTeacher` can be added here if needed.
    @Query("SELECT ct.course FROM CourseTeacher ct WHERE ct.teacher.id = :teacherId")
    List<Course> findCoursesByTeacherId(@Param("teacherId") Long teacherId);
}
