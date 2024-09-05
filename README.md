# Java Web Server with IoC Framework

## Description

This project is a lightweight Java-based web server designed to handle HTTP requests and serve static content (HTML, CSS, JS, images) as well as provide an IoC (Inversion of Control) framework for building web applications from POJOs. The server is capable of discovering and loading classes annotated with `@RestController`, automatically wiring routes to their methods using annotations like `@RequestMapping`, `@GetMapping`, and `@RequestParam`. 

The application allows the development of web services and integrates a reflection-based testing framework. It includes a minimal web example that demonstrates the reflective capabilities of Java and dynamic loading of components.

## Prerequisites

Before running the project, ensure you have the following installed:

- **Java Development Kit (JDK) 8 or higher**
- **Maven** for building the project and managing dependencies
- **Git** (optional for cloning the repository)

## How to Run the Project

1. **Clone the repository:**
   ```bash
   git clone https://github.com/bricenojuliana/AREP-springECI.git
   ```

2. **Navigate to the project directory:**
   ```bash
   cd AREP-springECI
   ```

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run the web server:**
   You can start the web server by executing the main class of the framework `SpringECI`:
   ```bash
   java -cp target/SpringECI-1.0-SNAPSHOT.jar co.edu.escuelaing.framework.SpringECI
   ```

5. **Access the web application:**
   Open your web browser and navigate to:
   ```
   http://localhost:8080/
   ```
   To access the REST services use the prefix /App
   ```
   http://localhost:8080/App
   ```

## Architecture

### Project Structure

```
C:.
├───main
│   ├───java
│   │   ├───co
│   │   │   └───edu
│   │   │       └───escuelaing
│   │   │           ├───application
│   │   │           │       App.java
│   │   │           │       CalculatorService.java
│   │   │           │       HelloService.java
│   │   │           │
│   │   │           └───framework
│   │   │               │   FrameworkConfig.java
│   │   │               │   SpringECI.java
│   │   │               │   WebServer.java
│   │   │               │
│   │   │               ├───annotations
│   │   │               │       GetMapping.java
│   │   │               │       RequestMapping.java
│   │   │               │       RequestParam.java
│   │   │               │       RestController.java
│   │   │               │       SpringECIApplication.java
│   │   │               │
│   │   │               └───enums
│   │   │                       RequestMethod.java
│   │   │
│   │   └───reflexionlab
│   │           ClassToBeTested.java
│   │           JUnitECI.java
│   │           MainExecutor.java
│   │           Reflexion.java
│   │           Test.java
│   │
│   └───resources
│           cocora.jpg
│           cristales.jpeg
│           example.png
│           guatavita.jpg
│           index.html
│           la-conejera.jpg
│           script.js
│           styles.css
│           villa-de-leyva.jpeg
│
└───test
    └───java
        └───co
            └───edu
                └───escuelaing
                    │   SpringECITest.java
                    │
                    ├───application
                    │       CalculatorServiceTest.java
                    │       HelloServiceTest.java
                    │
                    └───framework
                            TestService.java
                            WebServerTest.java
```

### Core Components

- **`App.java`**: The entry point for the application, initializes the server and services such as `HelloService` and `CalculatorService`.  
- **`WebServer.java`**: The server handles HTTP requests and responses, delivering static content (HTML, CSS, images) and routing requests to controllers.
- **`SpringECI.java`**: The IoC framework core that loads beans annotated with `@RestController`, routes HTTP requests, and manages request mappings.
- **`annotations`**: Includes custom annotations such as `@RestController`, `@GetMapping`, `@RequestMapping`, and `@RequestParam` for defining web service routes and parameters.
- **`services`**: Example services like `HelloService` and `CalculatorService` are loaded dynamically and expose REST endpoints.
  
### Flow of User Interaction

1. **Request**: A user sends an HTTP request from the browser (e.g., `GET /App/hello?name=John`).
2. **Request Handling**: The server (via `WebServer.java`) receives the request. If it's for a static file (HTML or image), it returns the file. If it's for a dynamic resource (like `/App/hello`), it forwards the request to the IoC framework.
3. **Framework Response**: `SpringECI` processes the request. It scans classes for methods annotated with `@RequestMapping` or `@GetMapping`, then invokes the method in the relevant controller (e.g., `HelloService`).
4. **Response**: The method returns a response (like `Hello, John!`), which the web server sends back to the user's browser.

### Example Interaction

For the endpoint `GET /App/hello?name=John`:
1. **User Request**: The user sends a request.
2. **Web Server**: Receives the request, passes it to `SpringECI`.
3. **IoC Framework**: Finds the `HelloService` and invokes its `hello()` method.
4. **Response**: Returns `Hello John!` to the user.

## Running Tests

### Automated Unit Tests
To run the automated tests included in the project:

1. **Run the tests with Maven:**
   ```bash
   mvn test
   ```
   ![image](https://github.com/user-attachments/assets/0a4d2dd6-1320-4870-b8a0-4f429e853dbb)
   

3. **Test Breakdown:**
   - **`WebServerTest.java`**: Verifies that the web server handles static resources and routes requests to the correct services.
   - **`SpringECITest.java`**: Ensures that annotated controllers are loaded correctly by the IoC framework.
   - **Service Tests**: Unit tests for `CalculatorServiceTest` and `HelloServiceTest` ensure that the business logic in the services works as expected.

Running these tests confirms that the framework can load annotated components, handle requests, and return appropriate responses.

### Testing on the Web

To manually test the web functionality:
1. Run the server as described above.
2. Open a browser and test endpoints like:
   ```
   http://localhost:8080/App/hello?name=John
   ```

   ![image](https://github.com/user-attachments/assets/b499aa36-6e09-4b4a-90ab-647dfe6b910a)

   This will return a dynamic greeting message. Test other endpoints to ensure the routes and services are working as expected.

   Other REST services available:
   - Pi:  
      ![image](https://github.com/user-attachments/assets/02700e2a-c3af-490e-9c07-19d2a2fc3381)

   - UUID:  
     ![image](https://github.com/user-attachments/assets/af708bb2-e0b3-457c-b2ad-a0b6911eabc1)
  
   - Bye:  
     ![image](https://github.com/user-attachments/assets/d525db8c-6c5d-4dc1-9ded-2866969380aa)

   - Random:  
      ![image](https://github.com/user-attachments/assets/b5adb920-5971-460f-a34c-7d9dc2c68be8)
  
   - Pow:  
     ![image](https://github.com/user-attachments/assets/e7eab2d1-d753-422b-ad85-ceb2395446f4)
  
   - Greet:  
     ![image](https://github.com/user-attachments/assets/b9d0c169-1932-4891-90f5-66b9a49ec8a5)
  
   - Add:  
     ![image](https://github.com/user-attachments/assets/7ba7bafe-18eb-4d8f-9529-031a4cc49852)
  
   - Subtract:  
     ![image](https://github.com/user-attachments/assets/7d13f4ea-6000-49db-b100-7a652081b996)
  
   - Multiply:  
     ![image](https://github.com/user-attachments/assets/5f9abfa0-bbdb-485a-9eea-37d801c12d8d)
  
   - Divide:  
     ![image](https://github.com/user-attachments/assets/c4bc7486-fa8d-4129-b36f-43553e0e00d9)
  
   - Sqrt:  
     ![image](https://github.com/user-attachments/assets/0ab5da4f-b203-4648-b934-c2de85baedec)

  Static service available on http://localhost:8080/ or http://localhost:8080/index.html
  ![image](https://github.com/user-attachments/assets/a030ba54-a171-4fb4-b609-7ac9aa8407fd)
  ![image](https://github.com/user-attachments/assets/c5a317c9-7b68-4ea8-9093-2be70e140674)
  ![image](https://github.com/user-attachments/assets/faf83a75-14b4-41d8-9e69-aa0ec99f6081)

## Using the SpringECI Framework

### Overview

The SpringECI framework enables you to define RESTful services with custom annotations. This guide will walk you through setting up a basic application using `@SpringECIApplication`, `@RestController`, `@RequestMapping`, and `@GetMapping`.

### Key Annotations

- **`@SpringECIApplication`**: Marks the main entry point of your application.
- **`@RestController`**: Defines a REST controller class.
- **`@RequestMapping`**: Maps requests to methods with specified HTTP methods.
- **`@GetMapping`**: A shortcut for `@RequestMapping` with GET method.

### Quick Example

#### 1. Create the Main Application Class

This class initializes the server. It should be annotated with `@SpringECIApplication` and include a `main` method to start the server.

```java
package com.example;

import com.example.framework.WebServer;
import com.example.framework.annotations.SpringECIApplication;

@SpringECIApplication
public class MainApp {
    public static void main(String[] args) {
        // Set the directory for static files
        WebServer.setStaticFilesDir("src/main/resources");

        // Start the web server
        WebServer.start();
    }
}
```

#### 2. Create a REST Controller

Define a class to handle HTTP requests. Annotate it with `@RestController`. Use `@RequestMapping` for general route mapping and `@GetMapping` for a specific GET request handler.

```java
package com.example;

import com.example.framework.annotations.GetMapping;
import com.example.framework.annotations.RequestMapping;
import com.example.framework.annotations.RestController;
import com.example.framework.enums.RequestMethod;

@RestController
public class GreetingController {

    @RequestMapping(value = "/greet", method = RequestMethod.GET)
    public String greet(@RequestParam("name") String name) {
        return "Hello, " + name + "!";
    }

    @GetMapping("/welcome")
    public String welcome(@RequestParam("name") String name) {
        return "Welcome, " + name + "!";
    }
}
```

### Running the Application

1. **Create the Main Class**: Implement the `MainApp` class with `@SpringECIApplication`.
2. **Define Controllers**: Create classes annotated with `@RestController` and use `@RequestMapping` and `@GetMapping` to map GET requests.
3. **Start the Server**: Run the `main` method in `MainApp` to start the server.
4. **Access Services**:
   - Visit `http://localhost:8080/greet?name=YourName` to see the greeting.
   - Visit `http://localhost:8080/welcome?name=YourName` to see the welcome message.


## Acknowledgments

- **Java Reflection API**: For enabling the dynamic discovery and loading of classes during runtime.
- **Apache Tomcat**: Inspiration for building a lightweight Java web server.
- **Spring Framework**: The concept of IoC and annotations in this project is inspired by Spring's approach to building web applications.
  
This project was developed as a hands-on exercise to learn about building web servers, reflection, and IoC frameworks in Java.

## Author

- **[Your Name]** - Developer of the Java Web Server and IoC Framework.
