package com.example.resource;

import com.example.model.Course;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.hibernate.reactive.mutiny.Mutiny;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;

@ApplicationScoped
public class CourseResource {
    @Inject
    Mutiny.SessionFactory sf;

    public Uni<Response> findAll() {
        return sf.withTransaction((s, t) -> s
                .createNamedQuery("courses.findAll", Course.class)
                .getResultList()
                .map(list ->
                        Response.ok(list).build()
                )
        );
    }

    public Uni<Course> findById(Long id) {
        return sf.withTransaction((s, t) -> s.find(Course.class, id));
    }

    public Uni<Response> persist(Course course) {
        if (course == null || course.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        return sf.withTransaction((s, t) -> s.persist(course))
                .replaceWith(Response.ok(course).status(Response.Status.CREATED)::build);
    }

    public Uni<Response> update(Integer id, Course course) {
        if (course == null || course.getName() == null) {
            throw new WebApplicationException("course name was not set on request.", 422);
        }
        return sf.withTransaction((s, t) -> s.find(Course.class, id)
                        .onItem().ifNull().failWith(new WebApplicationException("course missing from database.", NOT_FOUND))
                        // If entity exists then update it
                        .invoke(entity -> entity.setName(course.getName())))
                .map(entity -> Response.ok(entity).build());
    }

    public Uni<Response> delete(Integer id) {
        return sf.withTransaction((s, t) -> s.find(Course.class, id)
                        .onItem().ifNull().failWith(new WebApplicationException("course missing from database.", NOT_FOUND))
                        // If entity exists then delete it
                        .call(s::remove))
                .replaceWith(Response.ok().status(NO_CONTENT)::build);
    }
}
