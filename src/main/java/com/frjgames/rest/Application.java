package com.frjgames.rest;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * The entry point for the application startup using Jersey REST. I think?
 *
 * @author fridge
 */
public class Application extends ResourceConfig {

    public Application() {
        super.packages("com.frjgames.rest");
    }

}
