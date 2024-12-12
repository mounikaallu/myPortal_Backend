package com.portal.service;

import com.portal.model.Registration;
import com.portal.repository.RegistrationRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private RegistrationRepository repository;

    public LoginResponse login(String email, String password) {
    	  // Find the user by email
        Optional<Registration> optionalRegistration = repository.findByEmail(email);

        if (optionalRegistration.isEmpty()) {
            return new LoginResponse(false, "User not found.", null, null);
        }

        Registration registration = optionalRegistration.get();

        // Check if the password matches
        if (registration.getPassword().equals(password)) {
            return new LoginResponse(true, "Login successful.", registration.getUserRole(), registration.getId());
        } else {
            return new LoginResponse(false, "Invalid password. Please try again.", null, null);
        }
    }
}
