package com.portal.controller;

import com.portal.model.Registration;
import com.portal.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    // Fetch all teachers
    @GetMapping("/teachers")
    public ResponseEntity<List<Registration>> getTeachers() {
        List<Registration> teachers = staffService.getTeachers();
        return ResponseEntity.ok(teachers);
    }

    // Onboard a new teacher
    @PostMapping("/onboard-teacher")
    public ResponseEntity<?> onboardTeacher(@RequestBody Registration teacher) {
        try {
            Registration onboardedTeacher = staffService.onboardTeacher(teacher);
            return ResponseEntity.ok(onboardedTeacher);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    // Update teacher details
    @PostMapping("/update-teacher")
    public ResponseEntity<?> updateTeacher(@RequestBody Registration updatedTeacher) {
        try {
            boolean success = staffService.updateTeacher(updatedTeacher);
            if (success) {
                return ResponseEntity.ok("Teacher updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Teacher not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}
