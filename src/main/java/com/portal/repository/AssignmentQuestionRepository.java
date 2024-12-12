package com.portal.repository;

import com.portal.model.AssignmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentQuestionRepository extends JpaRepository<AssignmentQuestion, Long> {
    // Fetch all questions by assignment ID
    List<AssignmentQuestion> findByAssignmentId(Long assignmentId);
}
