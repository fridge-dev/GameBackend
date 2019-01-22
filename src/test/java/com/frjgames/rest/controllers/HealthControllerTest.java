package com.frjgames.rest.controllers;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;
import org.junit.Test;

/**
 * Tests the {@link HealthController} class.
 *
 * @author fridge
 */
public class HealthControllerTest {

    private HealthController healthController = new HealthController();

    @Test
    public void getHealth() {
        Response response = healthController.getHealth();

        assertEquals(200, response.getStatus());
    }

    @Test
    public void getHealthWithArg() {
        Response response = healthController.getHealthWithArg("hi");

        assertEquals(200, response.getStatus());
    }
}