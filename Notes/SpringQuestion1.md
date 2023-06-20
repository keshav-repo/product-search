### 1. On which parameters the connection pool size is calculated in Hibernates?
**Connection pool:** The connection pool size in a Spring Boot application refers to the maximum number of database connections that can be created and maintained in the connection pool. This pool of pre-established connections allows the application to reuse connections instead of creating a new connection for every database interaction, which can be expensive and time-consuming.

- The number of concurrent users: The number of concurrent users is the number of users that will be accessing the database at the same time. You need to make sure that the connection pool size is large enough to handle the number of concurrent users.
- The database load: The database load is the amount of work that is being done on the database. If the database is under a lot of load, you may need to increase the connection pool size.
- The type of database: Some databases, such as MySQL, are more efficient than others. If you are using a less efficient database, you may need to increase the connection pool size. \
#### **Other factors:** 
- If a typical request spends 50% of its time doing calculations and 50% on database connectivity you might only need 50 connections in your pool. Of course your application should release the db connection as early as possible.
- How much of their activity will generate database queries? 10 queries per page load? 1 query just on login? etc. etc.
- Size of the queries and the db obviously. Some queries run in milliseconds, some in minutes.
- The connection pool should be able to grow and shink based on actual needs. Log the numbers needed to do analysis on the running system, either through logging statements or through JMX surveillance. Consider setting up alerts for scenarios like "peak detected: more than X new entries had to be allocated in Y seconds", "connection was out of pool for more than X seconds" which will allow you to give attention to performance issues before they get real problems.
- [Optimal-number-of-connections-in-connection-pool](https://stackoverflow.com/questions/1208077/optimal-number-of-connections-in-connection-pool)
- Do performance testing using various tool and analyse the requirement 

### 2. Connection pool size in mysql 
The default value of max_connections in MySQL is often set to a relatively low value, such as 151 or 100. However, this value can be increased based on the specific requirements and available resources of your system.
To check the current value of max_connections in MySQL, you can run the following SQL query:
```
SHOW VARIABLES LIKE 'max_connections';
```
### 3. Significance of @Embeddable and @Embedded annotations
If some part of a entity is common among multiple entity, we can created seperate Embeddable class and then add that class as enbedded.
```
import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String street;
    private String city;
    private String zipCode;
    // Constructors, getters, and setters
    // ...
}

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Embedded
    private Address address;

    // Constructors, getters, and setters
    // ...
}
```
### 4. How can we achieve a centralized way of exception handling when we have 1000 or more classes in our application
- @ControllerAdvice annotation in Spring is used to define global exception handlers and apply them across multiple controllers. It allows you to centralize exception handling logic and provide consistent error responses throughout your application
```
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        // Custom exception handling logic
        // You can log the exception, customize the error response, etc.
        
        // Return a ResponseEntity with an appropriate HTTP status code and error message
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        // Custom exception handling logic for UserNotFoundException
        
        // Return a ResponseEntity with a specific HTTP status code and error message
        return new ResponseEntity<>("User not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    // Add more exception handlers as needed for different types of exceptions
    
    // ...
}
```
- @ExceptionHandler code sample in controller class 
```
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        // Logic to fetch user by ID from the database
        
        // Simulating an exception if user not found
        throw new UserNotFoundException("User not found with ID: " + id);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        // Custom exception handling logic for UserNotFoundException
        
        // Return a ResponseEntity with a specific HTTP status code and error message
        return new ResponseEntity<>("User not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
```
- @ResponseStatus in exception class 
```
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        // Logic to fetch user by ID from the database
        
        // Simulating an exception if user not found
        throw new UserNotFoundException("User not found with ID: " + id);
    }
}
```
- We can use default spring boot exception handler 
```
  server:
      error:
        include-message: always
        include-binding-errors: always
        include-stacktrace: never  ### never
        include-exception: false
  ```

### 5. Interceptors 
In Spring Boot, interceptors are used to intercept and process HTTP requests and responses. They provide a way to add cross-cutting concerns and perform common tasks such as logging, authentication, authorization, and request/response modification. Interceptors are executed before and after the execution of a handler method.

**LoggingInterceptor** In Spring Boot, a logging interceptor is a component that intercepts and logs incoming HTTP requests and outgoing responses. It is commonly used for capturing and recording information about the requests and responses in a structured and standardized format.
```
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // This method is called before the execution of the handler method
        // You can perform tasks such as logging, request validation, etc.
        System.out.println("LoggingInterceptor: Pre-handle");
        return true; // Return true to proceed with the request, or false to stop further processing
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // This method is called after the execution of the handler method, but before the view is rendered
        // You can modify the model or view
        System.out.println("LoggingInterceptor: Post-handle");
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // This method is called after the complete request has been processed
        // You can perform tasks such as logging the completion of the request, cleanup, etc.
        System.out.println("LoggingInterceptor: After completion");
    }
}

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;

    @Autowired
    public WebMvcConfig(LoggingInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the interceptor and define the URL patterns to be intercepted
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**"); // Intercepts all URLs
    }
}
```
### 5. Use of profilers and how can we implement in java?
Profilers are tools used in software development to analyze the performance and behavior of an application. They help identify bottlenecks, memory leaks, and other performance-related issues. Profilers provide insights into how the application utilizes system resources such as CPU, memory, and I/O operations.

In the context of Spring Boot, profilers can be used to analyze the performance of your application and identify areas for optimization. Here are a few popular profilers that can be used with Spring Boot:

1. VisualVM: VisualVM is a powerful profiler that comes bundled with the Java Development Kit (JDK). It provides a graphical interface to monitor the performance of your application, including CPU usage, memory consumption, thread activity, and more. VisualVM supports a wide range of plugins and is highly extensible.

2. Java Mission Control: Java Mission Control is another profiler tool provided by Oracle. It offers advanced profiling and monitoring capabilities for Java applications. It includes features like real-time JVM monitoring, flight recording, and analysis of performance data. Java Mission Control is typically used in production environments to monitor and optimize application performance.

3. YourKit: YourKit is a commercial Java profiler that offers advanced profiling capabilities. It provides detailed insights into CPU usage, memory allocation, and thread behavior. YourKit integrates well with Spring Boot applications and offers features like memory leak detection, performance tuning, and profiling of distributed systems.

To implement a profiler in your Spring Boot application, you typically need to follow these steps:

1. Add the profiler library or agent to your project's dependencies. The specific steps may vary depending on the profiler you choose.
2. Configure the profiler to attach to your Spring Boot application. This may involve setting up environment variables, JVM arguments, or configuration files. Refer to the profiler's documentation for detailed instructions.
3. Start your Spring Boot application and let it run while the profiler collects data. Depending on the profiler, you may need to enable specific profiling options or trigger a profiling session.
4. Analyze the profiler's output and identify areas for optimization or potential issues. Profilers typically provide visual representations, metrics, and detailed reports to help you understand the performance characteristics of your application.

Remember to use profilers judiciously and only when necessary, as they can introduce additional overhead and affect the performance of your application. It's also important to interpret the profiler's output correctly and prioritize optimizations based on actual bottlenecks and user experience.

Overall, using profilers can greatly assist in identifying and resolving performance-related issues in your Spring Boot application, leading to improved efficiency and user satisfaction.

### 6. 

