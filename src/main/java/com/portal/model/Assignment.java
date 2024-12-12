package com.portal.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Registration teacher;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AssignmentQuestion> questions = new HashSet<>();

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudentAssignment> studentAssignments = new HashSet<>();

    private String title;
    private String description;
    private LocalDateTime dueDate;

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

    public Set<AssignmentQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<AssignmentQuestion> questions) {
        this.questions = questions;
    }

    public Set<StudentAssignment> getStudentAssignments() {
        return studentAssignments;
    }

    public void setStudentAssignments(Set<StudentAssignment> studentAssignments) {
        this.studentAssignments = studentAssignments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    

}
