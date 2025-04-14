package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Student_Course_Rel")
public class StudentCourseRel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Student student;
    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    public StudentCourseRel() {
    }

    public StudentCourseRel(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public StudentCourseRel(Long id, Student student, Course course) {
        this.id = id;
        this.student = student;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
