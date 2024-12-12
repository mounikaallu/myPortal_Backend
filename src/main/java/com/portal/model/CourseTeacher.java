package com.portal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "course_teacher",
       uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "teacher_id"}))
public class CourseTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Registration teacher;

    public CourseTeacher() {}

    public CourseTeacher(Course course, Registration teacher) {
        this.course = course;
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

    public Registration getTeacher() {
        return teacher;
    }

    public void setTeacher(Registration teacher) {
        this.teacher = teacher;
    }
}
