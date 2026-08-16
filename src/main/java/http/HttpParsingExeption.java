package http;

public class HttpParsingExeption extends Exception{

    private final HttpStatusCode errorCode;

    public HttpParsingExeption(HttpStatusCode errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public HttpStatusCode getErrorCode() {
        return errorCode;
    }
}
