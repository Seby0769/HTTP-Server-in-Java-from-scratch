package http_server.core;

import http.HttpRequest;
import http.HttpResponse;

public interface HttpRouteHandler {
    HttpResponse handle(HttpRequest request);
}
