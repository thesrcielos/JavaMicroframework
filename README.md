# Java Microframework
This is a basic Web Java Framework implementation in Java using ServerSocket.

## Getting Started
These instructions will help you get a copy of the project running on your local machine for development and testing purposes.

### Prerequisites
You will need the following installed:
- [Java 17+](https://jdk.java.net/)
- [Maven 3.6+](https://maven.apache.org/install.html)
- Git (optional, for cloning)

### Installing
Clone the repository:
```bash
git clone https://github.com/thesrcielos/JavaHttpServer.git
cd JavaHttpServer
```

Build the project using Maven:
```bash
mvn clean install
```

### Running the Application

#### HTTP Server
Start the HTTP server on port 35000:
```bash
java -cp target/HttpServer-1.0-SNAPSHOT.jar org.eci.arep.HttpServer
```

The server will be available at `http://localhost:35000`

#### Usage and response Example of HTTP Server


Go to the browser and search localhost:35000
![](/assets/img1.png)
In the browser network panel can be found the requests made to the server
![](/assets/img1-1.png)
Usage of the first form with get
![](/assets/img2.png)
Http Request made and its response
![](/assets/img2-1.png)
Usage of the second form with post
![](/assets/img3.png)
Http Post request made and its response
![](/assets/img3-1.png)
#### Socket Examples
Run the server socket example:
```bash
java -cp target/HttpServer-1.0-SNAPSHOT.jar org.eci.arep.EchoServer
```

Run the client socket example (in a separate terminal):
```bash
java -cp target/HttpServer-1.0-SNAPSHOT.jar org.eci.arep.EchoClient
```

#### URL Examples
Demonstrate URL class methods:
```bash
java -cp target/HttpServer-1.0-SNAPSHOT.jar org.eci.arep.URLMethods
```

#### Web Page Reader
Read and display web pages:
```bash
java -cp target/HttpServer-1.0-SNAPSHOT.jar org.eci.arep.URLReader
```


## Features
- **HTTP Server**: Complete HTTP/1.1 server implementation supporting GET and POST methods
- **Socket Programming**: Examples of basic TCP socket communication between client and server
- **URL Utilities**: Comprehensive examples of Java URL class methods
- **Web Page Reader**: Utility to fetch web content
- **Static File Serving**: Serves HTML, CSS, JS, and image files from the `public/` directory

## API Endpoints
The HTTP server supports the following endpoints:
- `GET /` - Serves index.html from public directory
- `GET /*` - Serves static files (HTML, CSS, JS, images)
- `POST /app/hellopost?name=<name>` - Return an greeting message
- `GET /app/hello?name=<name>` - Return an greeting message

## Usage Examples

### Basic HTTP Request
```bash
curl http://localhost:35000/
```

### POST Request
```bash
curl -X POST http://localhost:35000/app/hellopost?name=diego
```
Example output
```
{"msg": "Hello diego"}
```
### Coding Style
The code follows the Google Java Style Guide and was formatted accordingly using Maven Checkstyle plugin.

## Running the tests

Run the automated unit tests with Maven:
```
mvn test
```
Unit Tests

Unit tests are written using JUnit and ensure the logic in **HttpServer** works as expected.
Some of the things that are being tested are:
* Http Server serves static files
* Http Response structure 
* Http Server API Endpoints
* Server validations

Example:
This test is testing that the required parameters are valid

```
@Test
void testMalformedQueryParameter() throws Exception {
    URI malformedUri = new URI("/app/hello?nameJohn");
    String response = HttpServer.helloService(malformedUri);
    assertTrue(response.contains("HTTP/1.1 400 Bad Request"));
    assertTrue(response.contains("Name not found"));
}
```
## Deployment
To package the application:
```bash
mvn package
```

Then run the packaged JAR:
```bash
java -jar target/HttpServer-1.0-SNAPSHOT.jar
```

## Javadoc
Code documentation is available in `target/site/apidocs/index.html` after running:
```bash
mvn javadoc:javadoc
```

## Built With
* [Java 17](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html) - Programming Language
* [Maven](https://maven.apache.org/) - Dependency Management
* [JUnit 5](https://junit.org/junit5/) - Testing Framework
* [Mockito](https://mockito.org/) - Mocking Framework

## Contributing
Please read **CONTRIBUTING.md** for details on our code of conduct and the process for submitting pull requests.

## Authors
* **Diego Armando Macia Diaz** 

## License
This project is licensed under the MIT License – see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments
* Inspired by understanding HTTP protocol fundamentals
* Socket programming concepts from Java networking documentation
* HTTP/1.1 specification