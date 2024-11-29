FROM openjdk:17
WORKDIR /
COPY target/gestion-sucursal-0.0.1-SNAPSHOT.jar Sucursal.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Sucursal.jar"]
