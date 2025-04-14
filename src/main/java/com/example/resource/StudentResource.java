package com.example.resource;

import com.example.model.Student;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;

@ApplicationScoped
public class StudentResource {
    @Inject
    SessionFactory sf;

    public Uni<Response> findAll() {
        return sf.withTransaction((s, t) -> s
                .createNamedQuery("students.findAll", Student.class)
                .getResultList()
                .map(list ->
                        Response.ok(list).build()
                )
        );
    }

    public Uni<Student> findById(Long id) {
        return sf.withTransaction((s, t) -> s.find(Student.class, id));
    }

    public Uni<Response> persist(Student student) {
        if (student == null || student.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        return sf.withTransaction((s, t) -> s.persist(student))
                .replaceWith(Response.ok(student).status(Response.Status.CREATED)::build);
    }

    public Uni<Response> update(Integer id, Student student) {
        if (student == null || student.getName() == null) {
            throw new WebApplicationException("student name was not set on request.", 422);
        }
        return sf.withTransaction((s, t) -> s.find(Student.class, id)
                        .onItem().ifNull().failWith(new WebApplicationException("student missing from database.", NOT_FOUND))
                        // If entity exists then update it
                        .invoke(entity -> entity.setName(student.getName())))
                .map(entity -> Response.ok(entity).build());
    }

    public Uni<Response> delete(Integer id) {
        return sf.withTransaction((s, t) -> s.find(Student.class, id)
                        .onItem().ifNull().failWith(new WebApplicationException("student missing from database.", NOT_FOUND))
                        // If entity exists then delete it
                        .call(s::remove))
                .replaceWith(Response.ok().status(NO_CONTENT)::build);
    }
}
