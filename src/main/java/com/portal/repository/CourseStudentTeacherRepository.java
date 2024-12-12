package com.portal.repository;

import com.portal.model.CourseStudentTeacher;
import com.portal.model.Course;
import com.portal.model.Registration;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseStudentTeacherRepository extends JpaRepository<CourseStudentTeacher, Long> {
    boolean existsByCourseAndStudent(Course course, Registration student);
    List<CourseStudentTeacher> findByStudentId(Long studentId);
    @Query("SELECT cst FROM CourseStudentTeacher cst WHERE cst.course.id = :courseId AND cst.teacher.id = :teacherId")
    List<CourseStudentTeacher> findByCourseIdAndTeacherId(@Param("courseId") Long courseId, @Param("teacherId") Long teacherId);

}
