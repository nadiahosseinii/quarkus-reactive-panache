package com.example.api;

import com.example.model.Student;
import com.example.resource.StudentResource;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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
                        Response.ok().entity(s).build())
                .onFailure()
                .recoverWithItem(e ->
                        Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Uni<Response> create(Student student) {
        return resource.persist(student).map(s -> Response.ok(s).build());
    }
}
