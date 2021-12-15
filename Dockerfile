FROM openjdk:17
COPY ./target/*.jar backend.jar
ENTRYPOINT ["java","-jar","backend.jar"]