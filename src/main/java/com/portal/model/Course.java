package com.portal.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseName;

    @Column(columnDefinition = "TEXT")
    private String courseDesc;

    @Column(nullable = false)
    private String semester;

    @ManyToMany
    @JoinTable(
        name = "course_teacher",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<Registration> teachers = new HashSet<>();
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CourseStudentTeacher> studentTeacherMappings = new HashSet<>();

    public Course() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Set<Registration> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Registration> teachers) {
        this.teachers = teachers;
    }
}
