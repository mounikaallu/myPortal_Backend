package com.portal.controller;

import com.portal.model.Registration;
import com.portal.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.portal.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private RegistrationService service;
    
 // Step 1: Initiate OTP
    @PostMapping("/initiate")
    public ResponseEntity<String> initiateRegistration(@RequestBody Registration registration) {
        try {
            String response = service.initiateOtpForEmail(registration);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (email == null || otp == null) {
            return ResponseEntity.badRequest().body("Email and OTP are required.");
        }

        String response = service.verifyOtpForEmail(email, otp);
        return ResponseEntity.ok(response);
    }


    // Step 3: Save Complete Registration Data
    @PostMapping("/complete")
    public ResponseEntity<Registration> completeRegistration(@RequestBody Registration registration) {
        Registration savedRegistration = service.completeRegistration(registration);
        return ResponseEntity.ok(savedRegistration);
    }

    @GetMapping
    public List<Registration> getAllRegistrations() {
        return service.getAllRegistrations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Registration> getRegistrationById(@PathVariable Long id) {
        return service.getRegistrationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Registration createRegistration(@RequestBody Registration registration) {
        return service.saveRegistration(registration);
    }

    @PutMapping("/{id}")
    public Registration updateRegistration(@PathVariable Long id, @RequestBody Registration updatedRegistration) {
        return service.updateRegistration(id, updatedRegistration);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        service.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }
    

}
