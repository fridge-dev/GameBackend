package com.frjgames.rest.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Simple controller which responds to health pings, to ensure the process is running.
 *
 * @author fridge
 */
@Path("/health")
public class HealthController {

    @GET
    public Response getHealth() {
        return Response.ok()
                .entity("healthy")
                .build();
    }

    @GET
    @Path("/{example-arg}")
    public Response getHealthWithArg(@PathParam("example-arg") final String arg) {
        return Response.ok()
                .entity("healthy-" + arg)
                .build();
    }

}
