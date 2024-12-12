package com.portal.controller;

import com.portal.model.Course;
import com.portal.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody Map<String, String> courseData) {
        String courseName = courseData.get("courseName");
        String courseDesc = courseData.get("courseDesc");
        String semester = courseData.get("semester");

        Course course = courseService.addCourse(courseName, courseDesc, semester);
        return ResponseEntity.ok(course);
    }

    @PostMapping("/{courseId}/assign-teachers")
    public ResponseEntity<Course> assignTeachers(
        @PathVariable Long courseId,
        @RequestBody Set<Long> teacherIds
    ) {
        Course course = courseService.assignTeachersToCourse(courseId, teacherIds);
        return ResponseEntity.ok(course);
    }
  
    //register course for student
    @PostMapping("/register")
    public ResponseEntity<String> registerForCourse(@RequestBody Map<String, Long> requestData) {
        Long studentId = requestData.get("studentId");
        Long courseId = requestData.get("courseId");
        Long teacherId = requestData.get("teacherId");

        String response = courseService.registerForCourse(studentId, courseId, teacherId);
        return ResponseEntity.ok(response);
    }
    
    // New endpoint to fetch courses for a specific student
    @GetMapping("/student/{studentId}/registered")
    public ResponseEntity<List<Course>> getRegisteredCoursesByStudent(@PathVariable Long studentId) {
        List<Course> courses = courseService.getCoursesByStudentIdWithTeacher(studentId);
        return ResponseEntity.ok(courses);
    }
    

    // New endpoint to fetch courses for a specific teacher
    @GetMapping("/teacher/{teacherId}/assigned")
    public ResponseEntity<List<Course>> getAssignedCoursesByTeacher(@PathVariable Long teacherId) {
        List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/{courseId}/teacher/{teacherId}/students")
    public ResponseEntity<List<Map<String, Object>>> getStudentsByCourseAndTeacher(
            @PathVariable Long courseId,
            @PathVariable Long teacherId) {
        List<Map<String, Object>> students = courseService.getStudentsByCourseAndTeacher(courseId, teacherId);
        return ResponseEntity.ok(students);
    }


}
