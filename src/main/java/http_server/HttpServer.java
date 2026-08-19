package http_server;

import http.HttpResponse;
import http.HttpStatusCode;
import http.HttpVersion;
import http_server.config.Configuration;
import http_server.config.ConfigurationManager;
import http_server.core.HttpRouter;
import http_server.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//DriverClass for the HTTP Server
public class HttpServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args){

        LOGGER.info("Server starting...");
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getMyCurrentConfiguration();

        LOGGER.info("Using port: " + conf.getPort());
        LOGGER.info("Using webroot: " + conf.getWebroot());

        try {
            HttpRouter router = new HttpRouter();

            router.addRoute("GET", "/", request -> {
                HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1, HttpStatusCode.SUCCESS_200_OK);
                response.addHeaders("Content-Type", "text/html");
                response.setBody("<html><head><title>Sebastian's Server</title></head><body><h1>Welcome to my backend!</h1><p>The router is officially dispatching requests.</p></body></html>");
                return response;
            });

            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot(), router);
            serverListenerThread.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
