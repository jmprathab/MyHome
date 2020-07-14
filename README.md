# MyHome Application

A Java application which helps people to manage their apartment.

Join us at Discord [here](https://discord.gg/CngACKh).

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
![Java CI with Gradle](https://github.com/jmprathab/MyHome/workflows/Java%20CI%20with%20Gradle/badge.svg?branch=master&event=push)
[![Discord](https://img.shields.io/discord/731769161361129523?label=discord)](https://discord.gg/CngACKh)
[![Good First Issue](https://img.shields.io/github/issues-raw/jmprathab/MyHome/good%20first%20issue?label=beginner%20friendly%20issues)](https://github.com/jmprathab/MyHome/issues?q=is%3Aopen+is%3Aissue+label%3A%22good+first+issue%22+label%3Aup-for-grabs)

## Prerequisites

* Gradle
* Java 8 (JDK)
* Docker for running service inside container (You can also run it without docker)

## Installing

1. Download the Project to local drive
2. Run `gradlew assemble`
3. Frontend is developed using React and is [here](https://github.com/jmprathab/MyHome-Web)

## Running using Docker

```shell
docker-compose build
docker-compose up
```

All required ports are mapped to localhost. REST API is accessible via port 8080.
http://localhost:8080/docs/swagger-ui.html hosts Swagger UI.

## Contributors

* [Prathab Murugan](https://github.com/jmprathab)
* [Jahir](https://github.com/Zedex7)
* [Gangadhar](https://github.com/gangadhargo)
* [mar731](https://github.com/mar731)

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
