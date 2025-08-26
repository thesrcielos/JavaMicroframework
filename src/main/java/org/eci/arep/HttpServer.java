package org.eci.arep;

import java.net.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Locale;
import java.io.*;
import java.util.Map;

public class HttpServer {
    private static final Map<String, HttpService> services = new HashMap<>();
    private static String WEB_ROOT_DIR = "src/main/resources/public";

    public static void get(String path, HttpService service){
        services.put(path, service);
    }

    public static void staticfiles(String path){
        WEB_ROOT_DIR = path;
    }

    public static void run() throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while(running){
            Socket clientSocket = null;
            HttpRequest request = new HttpRequest();
            try {
                System.out.println("Listo para recibir...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            URI requestUri = null;
            boolean firstLine = true;
            while ((inputLine = in.readLine()) != null) {
                if(firstLine){
                    String[] parts = inputLine.split(" ");
                    requestUri = new URI(parts[1]);
                    request.setMethod(parts[0]);
                    request.setUri(requestUri);
                    request.setHttpVersion(parts[2]);
                    firstLine = false;
                }
                else {
                    String[] headerParts = inputLine.split(":", 2);
                    if (headerParts.length == 2) {
                        request.addHeader(headerParts[0].trim(), headerParts[1].trim());
                    }
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            handleHttpRequest(request, out, clientSocket);

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException, URISyntaxException  {
       run();
    }

    public static void handleDynamicRequest(Socket clientSocket, HttpRequest request) throws IOException {
        HttpResponse response = new HttpResponse();
        URI requestUri = request.getUri();
        HttpService handler = services.get(requestUri.getPath());

        if (handler != null) {
            String body = handler.handle(request, response);
            response.setBody(body);
        } else {
            response.setStatus(404, "Not Found");
            response.setContentType("text/plain; charset=utf-8");
            response.setBody("Not found: " + requestUri.getPath());
        }

        response.send(clientSocket.getOutputStream());
    }


    public static void handleHttpRequest(HttpRequest request, PrintWriter out, Socket clientSocket) throws IOException {
        URI requestUri = request.getUri();
        if(requestUri != null){
            Path filePath = Path.of(WEB_ROOT_DIR, requestUri.getPath());
            if(Files.isDirectory(filePath)) {
                filePath = filePath.resolve("index.html");
            }
            if (Files.exists(filePath)) {
                String outputLine = "HTTP/1.1 200 OK\r\n"
                        + "content-type: " + getContentType(filePath) + "\r\n"
                        + "content-length: " + Files.size(filePath) + "\r\n"
                        + "\r\n";
                try (OutputStream outputStream = clientSocket.getOutputStream()) {
                    outputStream.write(outputLine.getBytes());
                    Files.copy(filePath, outputStream);
                }
            }else{
                handleDynamicRequest(clientSocket, request);
            }
        }
    }

    public static String getContentType(Path p) {
        String name = p.getFileName().toString().toLowerCase(Locale.ROOT);
        if (name.endsWith(".html") || name.endsWith(".htm")) return "text/html; charset=utf-8";
        if (name.endsWith(".css"))  return "text/css; charset=utf-8";
        if (name.endsWith(".js"))   return "application/javascript; charset=utf-8";
        if (name.endsWith(".json")) return "application/json; charset=utf-8";
        if (name.endsWith(".png"))  return "image/png";
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "image/jpeg";
        if (name.endsWith(".gif"))  return "image/gif";
        if (name.endsWith(".svg"))  return "image/svg+xml";
        if (name.endsWith(".ico"))  return "image/x-icon";
        return "application/octet-stream";
    }

}
