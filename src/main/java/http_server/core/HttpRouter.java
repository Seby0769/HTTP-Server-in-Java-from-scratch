package http_server.core;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatusCode;
import http.HttpVersion;

import java.util.HashMap;
import java.util.Map;

public class HttpRouter {
    private final Map<String, HttpRouteHandler> routes = new HashMap<>();

    public void addRoute(String method, String path, HttpRouteHandler handler) {
        String routeKey = method + " " + path;
        routes.put(routeKey, handler);
    }

    public HttpResponse route(HttpRequest request) {
        String routeKey = request.getMethod().name() + " " + request.getRequestTarget();

        HttpRouteHandler handler = routes.get(routeKey);

        if (handler != null) {
            return handler.handle(request);
        } else {
            HttpResponse notFoundResponse = new HttpResponse(HttpVersion.HTTP_1_1, HttpStatusCode.CLIENT_ERROR_404_NOT_FOUND);
            notFoundResponse.addHeaders("Content-Type", "text/html");
            notFoundResponse.setBody("<html><body><h1>404 Not Found</h1><p>The requested URL was not found on this server.</p></body></html>");
            return notFoundResponse;
        }
    }
}
