package com.example.resource;

import com.example.model.Student;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory;

import java.util.List;

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
        return Student.persist(student).replaceWith(student);
    }
//
//    public Uni<Response> update(Integer id, Student student) {
//        if (student == null || student.getName() == null) {
//            throw new WebApplicationException("student name was not set on request.", 422);
//        }
//        return sf.withTransaction((s, t) -> s.find(Student.class, id)
//                        .onItem().ifNull().failWith(new WebApplicationException("student missing from database.", NOT_FOUND))
//                        // If entity exists then update it
//                        .invoke(entity -> entity.setName(student.getName())))
//                .map(entity -> Response.ok(entity).build());
//    }
//
//    public Uni<Response> delete(Integer id) {
//        return sf.withTransaction((s, t) -> s.find(Student.class, id)
//                        .onItem().ifNull().failWith(new WebApplicationException("student missing from database.", NOT_FOUND))
//                        // If entity exists then delete it
//                        .call(s::remove))
//                .replaceWith(Response.ok().status(NO_CONTENT)::build);
//    }
}
