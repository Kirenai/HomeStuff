FROM openjdk:17-alpine
COPY target/home-stuff-0.0.1-SNAPSHOT.jar homestuff.jar
CMD ["java", "-jar", "homestuff.jar"]