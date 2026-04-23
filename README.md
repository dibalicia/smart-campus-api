# Smart Campus Sensor & Room Management API

## Overview
A RESTful API built with JAX-RS (Jersey) deployed on Apache Tomcat 9.
Manages campus rooms and IoT sensors with full CRUD operations,
sub-resource reading history, and comprehensive error handling.

## How to Build and Run:

#### Prerequisites
- Java 8+
- Maven 3.x
- Apache Tomcat 9
- NetBeans 26

### Steps
1. Clone the repository
2. Open project in NetBeans
3. Right-click project then Clean and Build
4. Right-click project then Run (auto-deploys to Tomcat)
5. API available at: `http://localhost:8080/api/v1`

## Sample curl Commands

### 1. Get API Info
```bash
curl http://localhost:8080/api/v1
```

### 2. Get all rooms
```bash
curl http://localhost:8080/api/v1/rooms
```

### 3. Create a new room
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"LAB-101","name":"Physics Lab","capacity":25}'
```

### 4. Filter sensors by type
```bash
curl http://localhost:8080/api/v1/sensors?type=CO2
```

### 5. Post a new sensor reading
```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":23.5}'
```

### 6. Try to delete a room that has sensors (expect 409)
```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301
```

### 7. Register a sensor with invalid roomId (expect 422)
```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"TEST-001","type":"Temperature","roomId":"FAKE-999"}'
```

## Answers to Coursework Questions

### Part 1: JAX-RS Resource Lifecycle
by default, a new instance of each resource class is created by JAX-RS for every incoming HTTP request (scope per-request). Therefore, the instance variables cannot keep state between requests because each request gets an object that has been just created. In order to maintain data across requests, a DataStore class containing static HashMaps is employed. If no synchronization mechanism exists, it will be possible for concurrent requests to suffer from race conditions; in other words, if two threads concurrently access and/or update the same map, then they will corrupt the data. Production level code would require use of either ConcurrentHashMap or synchronized blocks in order to avoid this situation.

### Part 1: HATEOAS
HATEOAS is a design approach for APIs in which API responses contain hyperlinks to other relevant data. Therefore, the client will be able to navigate itself through the API with out needing to read an external document describing the API. In this way, the amount of dependency between the client and server will be reduced. The server may change URL’s without breaking client code which follows the hyperlinks provided instead of hard coding paths.

### Part 2: IDs vs Full Objects
Returning only IDs will reduce both payload size and also reduce network bandwidth; in turn this improves performance with large collections. However returning only ID's means the client has to then make additional requests to get details on all rooms, increasing the number of round trips. On the other hand, returning full objects may increase payload but they would decrease the amount of requests that are needed. Minimal representations are best used when you need a summary list of items. Full object representation is best suited for detailed view of an item.

### Part 2: DELETE Idempotency
Yes, DELETE is idempotent with regard to deleting a room as per our implementation. When a DELETE request (or any subsequent requests) are made against a pre-existing room; the first delete will remove that room and send back HTTP status code 200 OK. If another DELETE request is then made again using the exact same room ID; because the room was deleted previously by the first DELETE request; the room can no longer be found and thus the server will send back HTTP status code 404 Not Found. Even though the status codes differ, the server state is identical after both calls; the room has been removed from existence. Therefore, we have met the definition of idempotency.

### Part 3: @Consumes Mismatch
Using the @Consumes(MediaType.APPLICATION_JSON) annotation in your JAX-RS class will tell JAX-RS that this resource only accepts requests in JSON format. If you send an HTTP request to this resource using Content-Type:text/plain or application/xml, JAX-RS will reject the request prior to it reaching your method, and as such will respond immediately with HTTP code 415 Unsupported Media Type. Therefore the application's business logic is never exposed to bad input.

### Part 3: @QueryParam vs Path Parameter
Query parameters (i.e. /sensors?type=CO2) are semantically appropriate for query filtering because they express optional additions to a collection, whereas path parameters (/sensors/type/CO2) indicate that "type/CO2" is an additional resource, which does not reflect the architecture of the service. In addition to being architecturally inappropriate, using query parameters in this way allows multiple query filters to be combined into a single URL (e.g., ?type=CO2&status=ACTIVE), significantly increasing flexibility for both search and filter operations.

### Part 4: Sub-Resource Locator Benefits
Using the Sub Resource Locator (SRL) design pattern enables large API's to be broken into separate classes that each have a specific purpose; thus making it easier for developers to understand and manage their code base. 
Sensor Reading Resource will contain the logic of how to read the sensor data. While Sensor Resource will handle the logic of managing sensors. This is beneficial because it makes the code easier to read, test and modify. Had we placed all nested paths in one resource class it would quickly become cumbersome. 
The SRL also makes passing context variables like "sensorId" from the main resource to the sub-resource clean by using constructors.

### Part 5: HTTP 422 vs 404
A 404 Not Found indicates that the specified URL does not exist. In contrast, an HTTP 422 Unprocessable Entity error indicates that the URL provided was correct, however there was some semantically invalid data in the JSON payload. So for example when you post a new sensor as a POST to /sensors with a roomId that has no value, the /sensors path is actually a valid route. it's just the content of your POST that is problematic. The use of HTTP 422 provides both better communication of this distinction to the user agent and also clearly communicates where the client should focus their debugging effort.

### Part 5: Stack Trace Security Risks
When the Java call stack is exposed to an attacker it will provide them with the exact name of the class, the name of the method (and thus which part of the app was running at the time), where the method was located (file path) as well as what version of a particular library was being used. An attacker would then be able to use this information to determine if there are any specific known vulnerabilities available in those versions of libraries, get an understanding of how the application's classes fit together so they could target a particular area of vulnerability, or create input data designed to force execution down one path of code over another.

### Part 5 – Logging Filters vs Manual Logging
JAX-RS Filters for Logging represent a Cross-Cutting Concern. There can be one Filter Class that will apply to all incoming requests and outgoing responses (i.e., All requests and All responses) automatically 
with no need to make any changes to any Resource Method. The manual calls to Logger.info() in each method would introduce a lot of duplicate code, they could easily be forgotten if you add new endpoint(s), and they would intermix your logging logic with your business logic which breaks the Separation Of Concerns Principle. Filters leave your Resource Classes clean while ensuring that there is always 
Consistent Logging on all aspects of Your Entire API automatically.
