FROM maven:3-amazoncorretto-21 as build
COPY src /home/app/src
COPY pom.xml /home/app
COPY monitoring /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM mcr.microsoft.com/openjdk/jdk:21-distroless
VOLUME /tmp
EXPOSE 8082
COPY --from=build /home/app/target/jenkins-integration-1.0.0.jar jenkins-integration.jar
ENTRYPOINT ["java","-jar","/jenkins-integration.jar"]