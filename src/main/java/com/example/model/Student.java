package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "STUDENTS")
@NamedQuery(name = "students.findAll", query = "SELECT s from Student s")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, name = "NAME")
    private String name;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public Student(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
