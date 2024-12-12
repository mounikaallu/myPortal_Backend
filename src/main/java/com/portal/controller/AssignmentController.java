package com.portal.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portal.model.Assignment;
import com.portal.service.AssignmentService;
import com.portal.service.StudentAssignmentService;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "http://localhost:3000")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private StudentAssignmentService studentAssignmentService;

    @PostMapping("/create")
    public ResponseEntity<String> createAssignment(@RequestBody Map<String, Object> requestData) {
        Long courseId = Long.valueOf(requestData.get("courseId").toString());
        Long teacherId = Long.valueOf(requestData.get("teacherId").toString());
        String title = requestData.get("title").toString();
        String description = requestData.get("description").toString();
        LocalDateTime dueDate = LocalDateTime.parse(requestData.get("dueDate").toString());

        String response = assignmentService.createAssignment(courseId, teacherId, title, description, dueDate);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{assignmentId}/add-question")
    public ResponseEntity<String> addQuestionToAssignment(@PathVariable Long assignmentId, @RequestBody Map<String, String> requestData) {
        String questionText = requestData.get("questionText");
        String response = assignmentService.addQuestionToAssignment(assignmentId, questionText);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{assignmentId}/assign-students")
    public ResponseEntity<String> assignToStudents(@PathVariable Long assignmentId, @RequestBody List<Long> studentIds) {
        String response = assignmentService.assignToStudents(assignmentId, studentIds);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/grade")
    public ResponseEntity<String> gradeAssignment(@RequestBody Map<String, Object> requestData) {
        Long studentAssignmentId = Long.valueOf(requestData.get("studentAssignmentId").toString());
        String grade = requestData.get("grade").toString();

        String response = assignmentService.gradeAssignment(studentAssignmentId, grade);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/submit")
    public ResponseEntity<String> submitAssignment(@RequestBody Map<String, String> requestData) {
        Long studentAssignmentId = Long.valueOf(requestData.get("studentAssignmentId"));
        String submissionText = requestData.get("submissionText");

        String response = studentAssignmentService.submitAssignment(studentAssignmentId, submissionText);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/course/{courseId}/teacher/{teacherId}")
    public ResponseEntity<List<Map<String, Object>>> getAssignmentsByTeacherAndCourse(
        @PathVariable Long courseId,
        @PathVariable Long teacherId
    ) {
        List<Map<String, Object>> assignments = assignmentService.getAssignmentsByTeacherAndCourse(courseId, teacherId);
        return ResponseEntity.ok(assignments);
    }
    
    @GetMapping("/course/{courseId}/teacher/{teacherId}/student/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getAssignmentsForStudentInCourseAndTeacher(
            @PathVariable Long courseId,
            @PathVariable Long teacherId,
            @PathVariable Long studentId) {
        List<Map<String, Object>> assignments = assignmentService.getAssignmentsForStudentInCourseAndTeacher(courseId, teacherId, studentId);
        return ResponseEntity.ok(assignments);
    }
}
