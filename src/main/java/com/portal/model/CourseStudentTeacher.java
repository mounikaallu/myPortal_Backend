package com.portal.model;

import jakarta.persistence.*;

@Entity
public class CourseStudentTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Registration student;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Registration teacher;

    public CourseStudentTeacher() {}

    public CourseStudentTeacher(Course course, Registration student, Registration teacher) {
        this.course = course;
        this.student = student;
        this.teacher = teacher;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Registration getStudent() {
        return student;
    }

    public void setStudent(Registration student) {
        this.student = student;
    }

    public Registration getTeacher() {
        return teacher;
    }

    public void setTeacher(Registration teacher) {
        this.teacher = teacher;
    }
}
