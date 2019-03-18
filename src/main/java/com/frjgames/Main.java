package com.frjgames;

import com.frjgames.rest.FrjHttpServer;
import java.io.IOException;
import java.util.Arrays;

/**
 * Entry point of application.
 *
 * @author fridge
 */
public class Main {

    public static void main(final String[] args) throws IOException {
        System.out.println("Received args: " + Arrays.toString(args));

        FrjHttpServer server = new FrjHttpServer();

        System.out.println(String.format("Jersey app started with WADL available at %sapplication.wadl", server.getBaseUri()));
        System.out.println("Hit enter to stop it...");
        int ignored = System.in.read();

        server.stopServer();
    }
}