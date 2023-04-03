FROM amazoncorretto:11
COPY target/*.jar /liquibase.jar
ENTRYPOINT ["java", "-jar", "/liquibase.jar"]
