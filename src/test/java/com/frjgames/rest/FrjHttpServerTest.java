package com.frjgames.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link FrjHttpServer} class. Does an integ test that actually listens/requests on the configured baseUri.
 *
 * @see com.frjgames.rest.controllers.HealthController
 *
 * @author fridge
 */
public class FrjHttpServerTest {

    private FrjHttpServer server;
    private WebTarget client;

    @Before
    public void setUp() throws Exception {
        server = new FrjHttpServer();
        server.startServer();

        Client clientConfiguration = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the clientConfiguration (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // clientConfiguration.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        this.client = clientConfiguration.target(server.getBaseUri());
    }

    /**
     * Calls {@link com.frjgames.rest.controllers.HealthController}.
     */
    @Test
    public void testHealthGet() {
        assertEquals("healthy", client.path("health").request().get(String.class));
    }

    @After
    public void tearDown() throws Exception {
        server.stopServer();
    }

}