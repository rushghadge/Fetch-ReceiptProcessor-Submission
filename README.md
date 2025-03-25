Please refer "Fetch Receipt Project.pdf" for screenshots of working endpoints and screenshots of documentation.

Running the Application with Docker
Jar is already build in Submitted project:
        target/receipt-processor-0.0.1-SNAPSHOT.jar

(OPTIONAL STEP) 
To build jar file execute below command on terminal : 
From the root project directory:
``` 
./mvnw clean package
```

(MANDATORY STEP)
Build the Docker Image, execute below command: 
docker build -t receipt-processor-app .

Run the Docker Container
docker run -p 8080:8080 receipt-processor-app

You can access application at :
http://localhost:8080


Requirements
- Java 17+ 
- Maven
- Docker