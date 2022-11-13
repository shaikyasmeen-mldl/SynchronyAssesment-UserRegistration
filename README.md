### SynchronyAssesment-UserRegistration

BranchName : master

# Implementations:
1. Logging using SLF4J 
2. Lombok annotations are being used to prevent boiler plate code.
3. SonarQube Quality is being taken care.
4. Comments, 3 layered architecture - Controller, Repository, Sevice is implemented.
5. Upload, view, delete images after authorizing the username and password.
6. H2 - InMemory Database is used.
7. This SpringBoot app is integrated with Imgur API to upload, view, delete images and Imgur internally is using OAuth2 for authentication.

Default port - 8080

#### ENDPOINTS

H2-Console - http://localhost:8080/h2-console

User Registration POST call

http://localhost:8080/synchrony/registration

View Specific User details GET call

http://localhost:8080/synchrony/viewuserimages/{userId}

Fetch All users GET call

http://localhost:8080/synchrony/users

Upload Image POST call

http://localhost:8080/synchrony/upload

Delete Image from IMGUR and Dabase based on delete Hash generated while Image upload DELETE call

http://localhost:8080/synchrony/delete/{deletehash}

##TECH STACK
Java 11
Spring Boot
REST API using Spring Boot
Maven Tool
IMGUR REST API


