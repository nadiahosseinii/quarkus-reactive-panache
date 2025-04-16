package com.example.api;

import com.example.model.Student;
import com.example.resource.StudentResource;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("students")
@RequestScoped
public class StudentRestApi {
    @Inject
    StudentResource resource;

    @GET
    public Uni<Response> getAll() {
        return resource.findAll()
                .map(s ->
                        Response.ok().entity(s).build());
    }

    @POST
    public Uni<Response> create(Student student) {
        return resource.persist(student).map(s -> Response.ok(s).build());
    }

    @POST
    @Path("/update")
    public Uni<Response> update(Student student) {
        return resource.update(student).map(s -> Response.ok(s).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return resource.delete(id).map(s -> Response.ok(s).build());
    }
}
