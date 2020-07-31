# SHIPPING API

This project contains the representation of an api responsible for performing actions related to deliveries.

#### PREREQUISITES

What things you need to install the software and how to install them:

```
Java 11
Maven 3.6.3
Docker
```

Install the tool for managing parallel versions of multiple Software Development Kits [SDK MAN](https://sdkman.io/install).

##### JAVA

Execute the following command:

```
sdk install java 11.0.8-amzn
```

##### MAVEN

Execute the following command:

```
sdk install maven 3.6.3
```

### RUNNING IN DOCKER 

These instructions will get you a copy of the project up and running on Docker for testing purposes.

Run the API execute the following command inside of the root folder:

```
 docker-compose up
```

### RUNNING IN DEVELOPMENT MODE

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

Create an instance of MySQL using the following command:

```
 docker run -e MYSQL_ROOT_PASSWORD=<root_password> -e MYSQL_DATABASE=<database_name> -e MYSQL_USER=<username> -e MYSQL_PASSWORD=<password> -p 3306:3306 mysql
```

Run the API execute the following command inside of the root folder:

```
 mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DDATA_SOURCE_URL=jdbc:mysql://localhost:3306/<database_name> -DDATA_SOURCE_USERNAME=<username> -DDATA_SOURCE_PASSWORD=<password>"
```

### RUNNING TESTS

#### SwaggerUI

1. Access the [swagger document](http://localhost:8080/swagger-ui.html).
2. Perform tests using the defined requests.
