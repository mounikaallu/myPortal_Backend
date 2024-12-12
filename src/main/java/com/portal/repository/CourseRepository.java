package com.portal.repository;

import com.portal.model.Course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {
	@Query("SELECT c FROM Course c JOIN FETCH c.teachers t WHERE c.semester = :semester")
	List<Course> findBySemesterWithTeachers(@Param("semester") String semester);

}
