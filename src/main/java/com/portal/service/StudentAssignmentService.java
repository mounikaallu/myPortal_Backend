package com.portal.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.model.StudentAssignment;
import com.portal.repository.StudentAssignmentRepository;

@Service
public class StudentAssignmentService {

    @Autowired
    private StudentAssignmentRepository studentAssignmentRepository;

    public String submitAssignment(Long studentAssignmentId, String submissionText) {
        Optional<StudentAssignment> studentAssignment = studentAssignmentRepository.findById(studentAssignmentId);

        if (studentAssignment.isEmpty()) {
            throw new RuntimeException("Assignment not found for the student");
        }

        // Fetch the assignment
        StudentAssignment assignment = studentAssignment.get();

        // Check if the assignment is already submitted
        if (assignment.isSubmitted()) {
            throw new RuntimeException("You have already submitted this assignment!");
        }

        // Update submission details
        assignment.setSubmissionText(submissionText);
        assignment.setSubmittedAt(LocalDateTime.now());
        assignment.setSubmitted(true); // Mark as submitted

        // Save the updated assignment
        studentAssignmentRepository.save(assignment);

        return "Assignment submitted successfully!";
    }
    
}
