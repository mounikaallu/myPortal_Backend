package com.portal.repository;

import com.portal.model.Registration;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
	 Optional<Registration> findByEmail(String email);
	 
	    @Query("SELECT r FROM Registration r WHERE r.userRole = :role")
	    List<Registration> findByUserRole(String role);
}
