package http;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private String requestTarget;
    private String httpVersion;

    public HttpMethod getMethod() {
        return method;
    }

    void setMethod(HttpMethod method) {
        this.method = method;
    }

    HttpRequest() {


    }
}
