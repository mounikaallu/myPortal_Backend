package com.portal.repository;

import com.portal.model.StudentAssignment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAssignmentRepository extends JpaRepository<StudentAssignment, Long> {
    // Check if a specific student has already been assigned a specific assignment
    boolean existsByAssignmentIdAndStudentId(Long assignmentId, Long studentId);
    List<StudentAssignment> findByStudentId(Long studentId);
}
