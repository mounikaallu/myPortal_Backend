package com.portal.service;

import com.portal.model.Registration;
import com.portal.repository.RegistrationRepository;
import com.portal.utils.OtpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import com.portal.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository repository;

    @Autowired
    private EmailService emailService;
    
    public String initiateOtpForEmail(Registration registration) {
        // Validate if email is provided
        if (registration.getEmail() == null || registration.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }

        // Check if a user with the provided email already exists
        Optional<Registration> existingUser = repository.findByEmail(registration.getEmail());

        // Create or update the user with OTP
        Registration userToSave = existingUser.orElse(new Registration());
        userToSave.setEmail(registration.getEmail());
        userToSave.setOtp(OtpGenerator.generateOtp());
        userToSave.setOtpExpirationTime(LocalDateTime.now().plusMinutes(5));

        // Save the registration record
        repository.save(userToSave);

        // Send OTP to the provided email
        emailService.sendOtp(userToSave.getEmail(), userToSave.getOtp());

        return "OTP sent to your email. Please verify it within 5 minutes.";
    }

 // Step 2: Verify OTP
    public String verifyOtpForEmail(String email, String otp) {
        Optional<Registration> optionalRegistration = repository.findByEmail(email);

        if (optionalRegistration.isPresent()) {
            Registration registration = optionalRegistration.get();

            // Log for debugging
            System.out.println("Email: " + email);
            System.out.println("Entered OTP: " + otp);
            System.out.println("Database OTP: " + registration.getOtp());
            System.out.println("OTP Expiration Time: " + registration.getOtpExpirationTime());
            System.out.println("Current Time: " + LocalDateTime.now());

            // Validate OTP
            if (registration.getOtp() != null
                    && registration.getOtp().equals(otp)
                    && LocalDateTime.now().isBefore(registration.getOtpExpirationTime())) {

                // Clear OTP on successful verification
                registration.setOtp(null);
                registration.setOtpExpirationTime(null);
                repository.save(registration);

                return "OTP verified successfully.";
            } else {
                return "Invalid or expired OTP.";
            }
        } else {
            return "Email not found. Please initiate registration first.";
        }
    }


    // Step 3: Save Complete Registration Data
    public Registration completeRegistration(Registration registration) {
        Optional<Registration> existingUser = repository.findByEmail(registration.getEmail());

        if (existingUser.isPresent()) {
            Registration existingRegistration = existingUser.get();

            // Update user data
            existingRegistration.setFirstName(registration.getFirstName());
            existingRegistration.setLastName(registration.getLastName());
            existingRegistration.setGender(registration.getGender());
            existingRegistration.setMobileNumber(registration.getMobileNumber());
            existingRegistration.setPassword(registration.getPassword());
            existingRegistration.setUserRole(registration.getUserRole());

            return repository.save(existingRegistration);
        } else {
            throw new RuntimeException("Email not found. Please initiate and verify OTP before completing registration.");
        }
    }



    public List<Registration> getAllRegistrations() {
        return repository.findAll();
    }

    public Optional<Registration> getRegistrationById(Long id) {
        return repository.findById(id);
    }

    public Registration saveRegistration(Registration registration) {
        // Save the registration
        Registration savedRegistration = repository.save(registration);

        // Send authentication email
        sendAuthenticationEmail(savedRegistration);

        return savedRegistration;
    }

    public Registration updateRegistration(Long id, Registration updatedRegistration) {
        return repository.findById(id).map(registration -> {
            registration.setFirstName(updatedRegistration.getFirstName());
            registration.setLastName(updatedRegistration.getLastName());
            registration.setGender(updatedRegistration.getGender());
            registration.setMobileNumber(updatedRegistration.getMobileNumber());
            registration.setEmail(updatedRegistration.getEmail());
            registration.setPassword(updatedRegistration.getPassword());
            registration.setUserRole(updatedRegistration.getUserRole());
            return repository.save(registration);
        }).orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    public void deleteRegistration(Long id) {
        repository.deleteById(id);
    }
    

    private void sendAuthenticationEmail(Registration registration) {
        String subject = "Welcome to MyPortal!";
        String body = "Hello " + registration.getFirstName() + ",\n\n" +
                "Thank you for registering on our platform. Please authenticate your email to activate your account.\n\n" +
                "Best regards,\nMyPortal Team";
        emailService.sendEmail(registration.getEmail(), subject, body);
    }
}
