# Home Stuff

This backend project is used to store, manage and retrieve household items. Have a way of knowing if household food is available for consumption or for provisioning.

# Tecnologies

* Maven
* Java
* DBMS MySQL
* Git

# Dependencies

* Spring Boot
    * Spring Framework
    * Spring Web
    * Spring Security
    * Spring Data JPA
    * Spring Validation
* Lombok
* MySQL Drive
* JWT
* Model Mapper
* Jetbrains Annotations

# Compilation

Home stuff use Maven as compilation system.

# Requirements

Have [Maven](https://maven.apache.org/download.cgi), [Git](https://git-scm.com) and [JDK17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) installed.

# Commands

Clone repository

```github
git clone https://github.com/Kirenai/home-stuff.git
```

Go to the root of the project and run the command. </br>
The environment variables passed to maven are used to configure the application-dev.yml file.

```maven
mvn clean install -DMYSQL_HOST={my_host} -DMYSQL_PORT={my_port} -DMYSQL_USER={my_root} -DMYSQL_PASSWORD={my_password}
```

Build the image in the root of the project. 

```docker
docker build -t homestuff:0.0.1 .
```

Finally, run the services with docker-compose.

```docker
docker-compose up -d
```




