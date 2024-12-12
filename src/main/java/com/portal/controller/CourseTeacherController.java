package com.portal.controller;

import com.portal.service.CourseTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/course-teacher")
public class CourseTeacherController {

    @Autowired
    private CourseTeacherService courseTeacherService;

    @PostMapping("/assign")
    public ResponseEntity<String> assignTeacherToCourse(@RequestBody Map<String, Long> requestData) {
        Long courseId = requestData.get("courseId");
        Long teacherId = requestData.get("teacherId");

        String response = courseTeacherService.assignTeacherToCourse(courseId, teacherId);
        return ResponseEntity.ok(response);
    }
}
