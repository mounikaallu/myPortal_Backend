package com.portal.service;

import com.portal.model.Course;
import com.portal.model.CourseStudentTeacher;
import com.portal.model.Registration;
import com.portal.repository.CourseRepository;
import com.portal.repository.CourseStudentTeacherRepository;
import com.portal.repository.CourseTeacherRepository;
import com.portal.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    

    @Autowired
    private RegistrationRepository registrationRepository;
    
    @Autowired
    private CourseStudentTeacherRepository courseStudentTeacherRepository;
    
    @Autowired
    private CourseTeacherRepository courseTeacherRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course addCourse(String courseName, String courseDesc, String semester) {
        Course course = new Course();
        course.setCourseName(courseName);
        course.setCourseDesc(courseDesc);
        course.setSemester(semester);
        return courseRepository.save(course);
    }

    public Course assignTeachersToCourse(Long courseId, Set<Long> teacherIds) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            throw new RuntimeException("Course not found");
        }
        Course course = courseOptional.get();

        Set<Registration> teachers = registrationRepository.findAllById(teacherIds).stream()
                .filter(teacher -> "teacher".equals(teacher.getUserRole()))
                .collect(java.util.stream.Collectors.toSet());

        course.setTeachers(teachers);
        return courseRepository.save(course);
    }
    
    public List<Course> getCoursesBySemester(String semester) {
        return courseRepository.findBySemesterWithTeachers(semester);
    }
    
    public String registerForCourse(Long studentId, Long courseId, Long teacherId) {
        Optional<Course> course = courseRepository.findById(courseId);
        Optional<Registration> student = registrationRepository.findById(studentId);
        Optional<Registration> teacher = registrationRepository.findById(teacherId);

        if (course.isEmpty() || student.isEmpty() || teacher.isEmpty()) {
            throw new RuntimeException("Invalid course, student, or teacher ID");
        }

        // Check for existing registration
        boolean exists = courseStudentTeacherRepository.existsByCourseAndStudent(course.get(), student.get());
        if (exists) {
            return "You are already registered for this course";
        }

        // Save registration
        CourseStudentTeacher registration = new CourseStudentTeacher(course.get(), student.get(), teacher.get());
        courseStudentTeacherRepository.save(registration);

        return "Registered successfully";
    }
    
    public List<Course> getCoursesByStudentIdWithTeacher(Long studentId) {
        // Fetch registrations based on the studentId
        List<CourseStudentTeacher> registrations = courseStudentTeacherRepository.findByStudentId(studentId);

        // Map to the course and include only the associated teacher
        List<Course> coursesWithTeacher = registrations.stream()
                .map(registration -> {
                    Course course = registration.getCourse();
                    Registration associatedTeacher = registration.getTeacher();

                    // Set the teachers list to include only the relevant teacher
                    course.setTeachers(Set.of(associatedTeacher));

                    return course;
                })
                .toList();

        return coursesWithTeacher;
    }
    
    public List<Course> getCoursesByTeacherId(Long teacherId) {
        // Validate teacherId
        Optional<Registration> teacher = registrationRepository.findById(teacherId);
        if (teacher.isEmpty() || !"teacher".equals(teacher.get().getUserRole())) {
            throw new RuntimeException("Invalid teacher ID");
        }

        // Fetch courses directly from `course_teacher` table
        return courseTeacherRepository.findCoursesByTeacherId(teacherId);
    }
    
    public List<Map<String, Object>> getStudentsByCourseAndTeacher(Long courseId, Long teacherId) {
        // Validate course and teacher
        Optional<Course> course = courseRepository.findById(courseId);
        Optional<Registration> teacher = registrationRepository.findById(teacherId);

        if (course.isEmpty() || teacher.isEmpty() || !"teacher".equals(teacher.get().getUserRole())) {
            throw new RuntimeException("Invalid course or teacher ID");
        }

        // Fetch students from the `CourseStudentTeacher` relationship
        List<CourseStudentTeacher> registrations = courseStudentTeacherRepository.findByCourseIdAndTeacherId(courseId, teacherId);

        // Map student details to a simplified structure
        return registrations.stream()
                .map(registration -> {
                    Registration student = registration.getStudent();
                    Map<String, Object> studentDetails = new HashMap<>();
                    studentDetails.put("id", student.getId());
                    studentDetails.put("firstName", student.getFirstName());
                    studentDetails.put("lastName", student.getLastName());
                    studentDetails.put("email", student.getEmail());
                    return studentDetails;
                })
                .collect(Collectors.toList());
    }


}
