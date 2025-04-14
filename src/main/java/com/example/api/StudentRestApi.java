package com.example.api;

import com.example.resource.StudentResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("students")
@RequestScoped
public class StudentRestApi {
    @Inject
    StudentResource resource;

    @GET
    public Response getAll() {
        return Response.status(Response.Status.OK).entity(resource.get()).build();
    }
}
