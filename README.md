# Simple Java HTTP Server

A custom, fully functional HTTP/1.1 server built entirely from scratch in Java.

This project demonstrates the core mechanics of network programming and backend architecture. It operates without relying on heavy web frameworks like Spring Boot or Tomcat. Instead, it directly manages raw TCP sockets, parses network byte streams into strict object-oriented HTTP messages, and dispatches requests through a custom routing engine.

## Core Features

* **Multithreaded Architecture:** Implements a `ServerListenerThread` that accepts incoming socket connections and immediately delegates them to concurrent `HttpConnectionWorkerThread` instances, preventing network deadlocks.
* **Strict Protocol Parsing:** Reads raw InputStreams to validate and parse the Request Line, Headers, and Message Body according to HTTP/1.1 specifications.
* **Regex Header Extraction:** Utilizes Regular Expressions to extract and map case-insensitive HTTP headers into instant-lookup HashMaps.
* **Custom Route Dispatcher:** Features a dynamic `Router` that maps specific combinations of HTTP Methods and URIs to distinct handler functions.
* **Robust Error Handling:** Automatically catches malformed requests, unsupported HTTP versions, and missing routes, safely returning precise HTTP status codes (e.g., `400 Bad Request`, `404 Not Found`, `501 Not Implemented`, `505 HTTP Version Not Supported`).
* **Configuration Management:** Parses runtime settings (port, webroot) from a static `http.json` file on startup.

## Technology Stack

* **Language:** Java
* **Build Tool:** Maven
* **Testing:** JUnit 5 (Exception testing, Regex validation, Byte stream mocking)
* **Configuration Parsing:** Jackson (jackson-core, jackson-databind)
* **Logging:** SLF4J & Logback

## Architecture Overview

The codebase strictly separates network mechanics from protocol logic, organized into three distinct packages:

1. **Protocol Implementation (`http`):** Contains the pure data models (`HttpRequest`, `HttpResponse`, `HttpStatusCode`) and the `HttpParser`. This package is completely isolated; it knows nothing about sockets, threads, or routing. Its only job is translating raw byte streams into structured HTTP objects and vice versa.
2. **Server Engine (`http_server.core`):** Contains the active machinery of the application. This includes the multithreaded connection handlers (`ServerListenerThread`, `HttpConnectionWorkerThread`) and the `HttpRouter` that dispatches incoming requests to specific execution logic.
3. **Configuration Management (`http_server.config`):** Handles application startup by parsing the external `http.json` file into memory using Jackson, injecting settings like the port and webroot into the main application thread.

## Getting Started

### Prerequisites
* Java Development Kit (JDK)
* Maven

### Build and Run

1. Clone the repository:
```bash
git clone [https://github.com/yourusername/simple_http_server.git](https://github.com/yourusername/simple_http_server.git)
cd simple_http_server
```

2. Build the project using Maven:
```bash
mvn clean install
```

3. Configure the server:
   Edit `src/main/resources/http.json` to define your target port and webroot directory.
```json
{
  "port": 8080,
  "webroot": "/tmp"
}
```

4. Run the main driver class:
   Execute `http_server.HttpServer`.

## Usage & Routing Example

The server's routing engine allows for clean, declarative endpoint definitions. Routes are configured in the main driver class before starting the listener thread.

```java
HttpRouter router = new HttpRouter();

// Define a basic GET endpoint
router.addRoute("GET", "/", request -> {
    HttpResponse response = new HttpResponse(HttpVersion.HTTP_1_1, HttpStatusCode.SUCCESS_200_OK);
    response.addHeader("Content-Type", "text/html");
    response.setBody("<html><body><h1>Welcome to the custom HTTP Server</h1></body></html>");
    return response;
});

// The server automatically handles 404s for unregistered routes
```