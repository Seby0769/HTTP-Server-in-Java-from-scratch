package http_server.core;

import http.*;
import http_server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//FOR PROCESSING CONNECTIONS AT THE SAME TIME / AVOID QUEUE
public class HttpConnectionWorkerThread extends Thread{

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    private Socket socket;
    public HttpConnectionWorkerThread (Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            HttpParser parser = new HttpParser();
            try {
                HttpRequest request = parser.parseHttpRequest(inputStream);
                LOGGER.info("Successfully parsed request for target: " + request.getRequestTarget());
            } catch (HttpParsingException e) {
                LOGGER.error("Failed to parse request", e);
                HttpResponse errorResponse = new HttpResponse(HttpVersion.HTTP_1_1, e.getErrorCode());
                outputStream.write(errorResponse.build().getBytes());
                return;
            }

            String html = "<html><head><title>Simple Java HTTP Server</title></head><body><h1>This page was served using my http server</h1></body></html>";

            HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1, HttpStatusCode.SUCCESS_200_OK);
            response.addHeaders("Content-Type", "text/html");
            response.setBody(html);

            outputStream.write(response.build().getBytes());

            LOGGER.info("Connection Processing Finished.");
        } catch (IOException e) {
            LOGGER.error("Promblem with communication", e);
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }
}
