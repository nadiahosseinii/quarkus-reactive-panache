package com.example.resource;

import com.example.model.Student;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory;

import java.util.List;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@ApplicationScoped
public class StudentResource {
    @Inject
    SessionFactory sf;

    public Uni<List<Student>> findAll() {
        return Student.listAll();
    }

    public Uni<Student> findById(Long id) {
        return Student.findById(id);
    }

    public Uni<Student> persist(Student student) {
        if (student == null || student.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        return Panache.withTransaction(student::persist).map(s -> student);
    }

    public Uni<Student> update(Student student) {
        if (student == null || student.getName() == null) {
            throw new WebApplicationException("student name was not set on request.", 422);
        }
        return sf.withTransaction((s, t) ->
                        s.find(Student.class, student.getId())
                                .onItem()
                                .ifNull()
                                .failWith(new WebApplicationException("student missing from database.", NOT_FOUND))
                                .invoke(entity -> entity.setName(student.getName())))
                .map(entity -> entity);
    }

    public Uni<Student> delete(Long id) {
        return sf.withTransaction((s, t) ->
                s.find(Student.class, id)
                        .onItem()
                        .ifNull()
                        .failWith(new WebApplicationException("student missing from database.", NOT_FOUND))
                        .call(s::remove)
                        .map(entity -> entity));
    }
}
