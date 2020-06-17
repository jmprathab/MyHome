# MyHome Application

A Java application which helps people to manage their apartment.

![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)
![Java CI with Gradle](https://github.com/jmprathab/MyHome/workflows/Java%20CI%20with%20Gradle/badge.svg?branch=master&event=push)

## Prerequisites

* Gradle
* Java 11 (JDK)
* Docker for running service inside container (You can also run it without docker)

## Installing

1. Download the Project to local drive
2. Run `gradlew assemble`

## Running using Docker

```shell
docker-compose build
docker-compose up
```

All required ports are mapped to localhost. REST API is accessible via port 8080.
http://localhost:8080/docs/swagger-ui.html hosts Swagger UI.

## Authors

* [Prathab Murugan](https://github.com/jmprathab)

## License

This project is licensed under the Apache License - see the [LICENSE.md](LICENSE.md) file for details

## Contribution

Feel free to contribute to the project. Please make sure to follow the below list before contributing.

* Read `CODE_OF_CONDUCT.md`
* This project uses coding style from https://github.com/square/java-code-styles
* Make sure all test cases pass before requesting for a PR.

## Acknowledgments

This project relies on the following projects

* Spring Framework
* Docker
