package com.portal.repository;

import com.portal.model.Assignment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
	@Query("SELECT a FROM Assignment a WHERE a.course.id = :courseId AND a.teacher.id = :teacherId")
	List<Assignment> findAssignmentsByCourseAndTeacher(@Param("courseId") Long courseId, @Param("teacherId") Long teacherId);
	

    // Add any custom query methods if needed
}
