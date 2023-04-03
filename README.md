# main-backend-java
[![Application CI Build](https://github.com/librbary/main-backend-java/actions/workflows/maven.yml/badge.svg?branch=develop)](https://github.com/librbary/main-backend-java/actions/workflows/maven.yml)

## liBRBary Application
> [liBRBary - Buy, Rental, Barter](https://librbary.github.io/main-frontend-react).

## Local workspace setup guidelines

### Pre-requisite 
* Install [Java 17](https://adoptium.net/temurin/releases/) on your system
* Install [Maven](https://maven.apache.org/download.cgi) if not using bundled maven
* Install [Docker](https://www.docker.com/products/docker-desktop/) Application

### Steps to setup liBRBary backend application in local

* Run Docker Engine.

* After checking out your feature branch in your local, first step is to build the application using Maven <br />
  ``` mvn clean install ```

* To check code indentation, use below command <br />
  ``` mvn spotless:check ```

* To organize your code and fix code indentation, use below command <br />
  ``` mvn spotless:apply ```

* To run only junit tests, use below command <br />
  ``` mvn clean test ```


###### Cucumber Tests
* To run Functional Tests BDDs, use below command <br />
  ``` mvn clean test -Dcucumber.filter.tags="@FunctionalTest" -Dcucumber.glue="com.librbary.main.bdd.api.config.e0,com.librbary.main.bdd.api.stepdefs" ```
  
* To run PostDeployment Tests BDDs, use below command <br />
  ``` mvn clean test -Dcucumber.filter.tags="@PostDeploymentTests" -Dcucumber.glue="com.librbary.main.bdd.api.config.e1,com.librbary.main.bdd.api.stepdefs" ```

* To run a particular *feature* or *scenario* for **Functional Tests**, please add the below configurations in 'Edit Configuration...' <br />
 _Glue_: com.librbary.main.bdd.api.config.e0 com.librbary.main.bdd.api.stepdefs

* To run a particular *feature* or *scenario* for **PostDeployment Tests**, please add the below configurations in 'Edit Configuration...' <br />
  _Glue_: com.librbary.main.bdd.api.config.e0 com.librbary.main.bdd.api.stepdefs

## CI/CD Pipeline
We are using **github-actions** for continuous integration and continuous delivery. You can refer the workflows yml to add/update the jobs in the build/deploy workflow yml.


## Useful Links

* [Swagger UI](http://localhost:8443/swagger-ui/index.html) - for API documentation and testing *(LOCAL)*
* [Swagger YML](http://localhost:8443/v3/api-docs.yml) - Swagger specifications in yml format *(LOCAL)*
* [GitHub Actions](https://github.com/librbary/main-backend-java/actions) - for CI/CD build and deployment
* [Application Health Check](http://localhost:8443/actuator/health) - Actuator endpoint for application health check *(LOCAL)*
* [E1 Deployment Info](http://localhost:8443/actuator/info) - To check last deployed branch and commit id *(LOCAL)*