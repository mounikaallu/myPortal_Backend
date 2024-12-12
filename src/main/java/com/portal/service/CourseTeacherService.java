package com.portal.service;

import com.portal.model.Course;
import com.portal.model.CourseTeacher;
import com.portal.model.Registration;
import com.portal.repository.CourseRepository;
import com.portal.repository.CourseTeacherRepository;
import com.portal.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CourseTeacherService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CourseTeacherRepository courseTeacherRepository;

    public String assignTeacherToCourse(Long courseId, Long teacherId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        Optional<Registration> teacherOptional = registrationRepository.findById(teacherId);

        if (courseOptional.isEmpty() || teacherOptional.isEmpty()) {
            return "Invalid course or teacher ID.";
        }

        Course course = courseOptional.get();
        Registration teacher = teacherOptional.get();

        // Check if the teacher is already assigned
        boolean isAssigned = courseTeacherRepository.existsById(courseId); // Adjust based on custom methods if needed
        if (isAssigned) {
            return "Teacher is already assigned to this course.";
        }

        // Save the teacher assignment
        CourseTeacher courseTeacher = new CourseTeacher(course, teacher);
        courseTeacherRepository.save(courseTeacher);

        return "Teacher assigned successfully.";
    }
}
