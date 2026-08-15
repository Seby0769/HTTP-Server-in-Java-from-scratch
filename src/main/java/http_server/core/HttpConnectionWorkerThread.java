package http_server.core;

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

            int _byte;
            while ((_byte = inputStream.read()) >= 0) {
                System.out.print((char)_byte);
            }

            String html = "<html><head><title>Simple Java HTTP Server</title></head><body><h1>This page was served using my http server</h1></body></html>";

            final String CRLF = "\r\n"; //13, 10

            String response =
                    "HTTP/1.1 200 OK" + CRLF + //Status Line : HTTP VERSION RESPONSE_CODE RESPONSE_MESSAGE
                            "Content-Length: " + html.getBytes().length + CRLF + //HEADER
                            CRLF +
                            html +
                            CRLF + CRLF;

            outputStream.write(response.getBytes());

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
