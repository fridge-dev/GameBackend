package com.frjgames.rest;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * TODO
 *
 * @author TODO
 */
public class Application extends ResourceConfig {

    public Application() {
        super.packages("com.frjgames.rest");
    }

}
