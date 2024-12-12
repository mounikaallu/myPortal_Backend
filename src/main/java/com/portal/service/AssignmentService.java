package com.portal.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.model.Assignment;
import com.portal.model.AssignmentQuestion;
import com.portal.model.Course;
import com.portal.model.Registration;
import com.portal.model.StudentAssignment;
import com.portal.repository.AssignmentQuestionRepository;
import com.portal.repository.AssignmentRepository;
import com.portal.repository.CourseRepository;
import com.portal.repository.RegistrationRepository;
import com.portal.repository.StudentAssignmentRepository;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RegistrationRepository registrationRepository;
    
    @Autowired
    private StudentAssignmentRepository studentAssignmentRepository;
    
    @Autowired
    private AssignmentQuestionRepository assignmentQuestionRepository;

    public String createAssignment(Long courseId, Long teacherId, String title, String description, LocalDateTime dueDate) {
        Optional<Course> course = courseRepository.findById(courseId);
        Optional<Registration> teacher = registrationRepository.findById(teacherId);

        if (course.isEmpty() || teacher.isEmpty() || !"teacher".equals(teacher.get().getUserRole())) {
            throw new RuntimeException("Invalid course or teacher");
        }

        Assignment assignment = new Assignment();
        assignment.setCourse(course.get());
        assignment.setTeacher(teacher.get());
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDueDate(dueDate);

        assignmentRepository.save(assignment);
        return "Assignment created successfully!";
    }
    
    public String addQuestionToAssignment(Long assignmentId, String questionText) {
        Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);

        if (assignment.isEmpty()) {
            throw new RuntimeException("Assignment not found");
        }

        AssignmentQuestion question = new AssignmentQuestion();
        question.setAssignment(assignment.get());
        question.setQuestionText(questionText);

        assignmentQuestionRepository.save(question);
        return "Question added successfully!";
    }
    
    public String assignToStudents(Long assignmentId, List<Long> studentIds) {
        Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);

        if (assignment.isEmpty()) {
            throw new RuntimeException("Assignment not found");
        }

        for (Long studentId : studentIds) {
            Optional<Registration> student = registrationRepository.findById(studentId);

            if (student.isEmpty() || !"student".equals(student.get().getUserRole())) {
                continue; // Skip invalid or non-student users
            }

            StudentAssignment studentAssignment = new StudentAssignment();
            studentAssignment.setAssignment(assignment.get());
            studentAssignment.setStudent(student.get());

            studentAssignmentRepository.save(studentAssignment);
        }

        return "Assignment assigned to students successfully!";
    }
    
    public String gradeAssignment(Long studentAssignmentId, String grade) {
        Optional<StudentAssignment> studentAssignment = studentAssignmentRepository.findById(studentAssignmentId);

        if (studentAssignment.isEmpty()) {
            throw new RuntimeException("Student assignment not found");
        }

        studentAssignment.get().setGrade(grade);
        studentAssignmentRepository.save(studentAssignment.get());

        return "Assignment graded successfully!";
    }
    
    public List<Map<String, Object>> getAssignmentsByTeacherAndCourse(Long courseId, Long teacherId) {
        List<Assignment> assignments = assignmentRepository.findAssignmentsByCourseAndTeacher(courseId, teacherId);

        return assignments.stream()
                .map(assignment -> Map.of(
                    "id", assignment.getId(),
                    "title", assignment.getTitle(),
                    "description", assignment.getDescription(),
                    "dueDate", assignment.getDueDate(),
                    "questions", assignment.getQuestions().stream()
                                       .map(AssignmentQuestion::getQuestionText) // Directly map question text
                                       .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getAssignmentsForStudentInCourseAndTeacher(Long courseId, Long teacherId, Long studentId) {
        // Fetch all assignments for the course and teacher
        List<Assignment> assignments = assignmentRepository.findAssignmentsByCourseAndTeacher(courseId, teacherId);

        // Filter assignments that are specific to the given student
        List<StudentAssignment> studentAssignments = studentAssignmentRepository.findByStudentId(studentId);

        return assignments.stream()
                .filter(assignment -> studentAssignments.stream()
                        .anyMatch(sa -> sa.getAssignment().getId().equals(assignment.getId())))
                .map(assignment -> {
                    Map<String, Object> assignmentMap = new HashMap<>();
                    assignmentMap.put("id", assignment.getId());
                    assignmentMap.put("title", assignment.getTitle());
                    assignmentMap.put("description", assignment.getDescription());
                    assignmentMap.put("dueDate", assignment.getDueDate());
                    assignmentMap.put("submitted", studentAssignments.stream()
                            .filter(sa -> sa.getAssignment().getId().equals(assignment.getId()))
                            .findFirst()
                            .map(StudentAssignment::isSubmitted)
                            .orElse(false));
                    assignmentMap.put("grade", studentAssignments.stream()
                            .filter(sa -> sa.getAssignment().getId().equals(assignment.getId()))
                            .findFirst()
                            .map(StudentAssignment::getGrade)
                            .orElse(null));
                    assignmentMap.put("submissionText", studentAssignments.stream()
                            .filter(sa -> sa.getAssignment().getId().equals(assignment.getId()))
                            .findFirst()
                            .map(StudentAssignment::getSubmissionText)
                            .orElse(null));
                    // Include questions
                    assignmentMap.put("questions", assignment.getQuestions().stream()
                            .map(AssignmentQuestion::getQuestionText)
                            .collect(Collectors.toList()));
                    return assignmentMap;
                })
                .collect(Collectors.toList());
    }

}
