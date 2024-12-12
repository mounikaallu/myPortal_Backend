package com.portal.service;

import com.portal.model.Registration;
import com.portal.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    @Autowired
    private RegistrationRepository repository;

    // Fetch all teachers
    public List<Registration> getTeachers() {
        return repository.findByUserRole("teacher");
    }

    // Onboard a new teacher
    public Registration onboardTeacher(Registration teacher) {
        // Check if email already exists
        Optional<Registration> existingTeacher = repository.findByEmail(teacher.getEmail());
        if (existingTeacher.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Set role and save the teacher
        teacher.setUserRole("teacher");
        return repository.save(teacher);
    }

    // Update teacher details
    public boolean updateTeacher(Registration updatedTeacher) {
        Optional<Registration> existingTeacher = repository.findById(updatedTeacher.getId());
        if (existingTeacher.isPresent()) {
            Registration teacher = existingTeacher.get();
            teacher.setFirstName(updatedTeacher.getFirstName());
            teacher.setLastName(updatedTeacher.getLastName());
            teacher.setMobileNumber(updatedTeacher.getMobileNumber());
            teacher.setGender(updatedTeacher.getGender());
            teacher.setOtp(null);
            teacher.setOtpExpirationTime(null);
            repository.save(teacher);
            return true;
        }
        return false;
    }
}
