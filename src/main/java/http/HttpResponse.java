package http;

import java.util.HashMap;

public class HttpResponse extends HttpMessage{
    private HttpVersion httpVersion;
    private HttpStatusCode statusCode;
    private HashMap<String, String> headers = new HashMap<>();
    private String body;

    public HttpResponse(HttpVersion httpVersion, HttpStatusCode statusCode){
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
    }

    public void addHeaders(String name, String value){
        headers.put(name.toLowerCase(), value);
    }

    public void setBody(String body){
        this.body = body;
        addHeaders("Content-Length", String.valueOf(body.getBytes().length));
    }

    public String build(){
        StringBuilder responseBuilder = new StringBuilder();
        final String CRLF = "\r\n";

        //status line construction("HTTP/1.1 200 OK\r\n")
        responseBuilder.append(httpVersion.LITERAL).append(" ")
                .append(statusCode.STATUS_CODE).append(" ")
                .append(statusCode.MESSAGE).append(CRLF);

        // header construction
        for (String headerName : headers.keySet()) {
            responseBuilder.append(headerName).append(": ").append(headers.get(headerName)).append(CRLF);
        }

        //end of header
        responseBuilder.append(CRLF);

        //append body
        if (body != null && !body.isEmpty()) {
            responseBuilder.append(body);
        }

        return responseBuilder.toString();
    }

}
