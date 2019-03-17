package com.frjgames.rest;

import java.net.URI;
import lombok.Getter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Abstraction for our HTTP server/servlet.
 *
 * @author fridge
 */
public class FrjHttpServer {

    /**
     * Base URI the Grizzly HTTP server will listen on.
     */
    @Getter
    private final String baseUri = "http://localhost:8080/fridge/rest/v1/";

    /**
     * scans for JAX-RS resources and providers in com.frjgames.rest java-package.
     */
    private final ResourceConfig resourceConfig = new ResourceConfig().packages("com.frjgames.rest");

    /**
     * Grizzly HTTP server exposing JAX-RS resources defined in this application.
     */
    private HttpServer httpServer;

    /**
     * Starts server listening on port.
     *
     * Create and start a new instance of grizzly http server exposing the Jersey application at baseUri.
     */
    public void startServer() {
        httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), resourceConfig);

    }

    /**
     * Stop listening on the server port.
     */
    public void stopServer() {
        httpServer.shutdownNow();
    }
}
