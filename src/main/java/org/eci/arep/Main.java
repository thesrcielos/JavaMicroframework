package org.eci.arep;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.eci.arep.HttpServer.get;
import static org.eci.arep.HttpServer.staticfiles;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        staticfiles("public");
        get("/hello", (req, resp) -> "Hello " + req.getValues("name"));
        get("/pi", (req, resp) -> {
            return String.valueOf(Math.PI);
        });
        get("/app/hello", (req, res) -> "Hello");
        HttpServer.run();
    }

}
