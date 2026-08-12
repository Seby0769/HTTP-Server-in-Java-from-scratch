package http_server;

import http_server.config.Configuration;
import http_server.config.ConfigurationManager;
import http_server.core.ServerListenerThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//DriverClass for the HTTP Server
public class HttpServer {
    public static void main(String[] args){

        System.out.println("Server starting...");

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getMyCurrentConfiguration();

        System.out.println("Using port: " + conf.getPort());
        System.out.println("Using webroot: " + conf.getWebroot());

        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
