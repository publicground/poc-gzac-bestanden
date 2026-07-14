# FROM eclipse-temurin:17-alpine
FROM amazoncorretto:17-alpine

ADD /build/libs/gzac-sd-zgw-backend.war /app.war

# !! This cmd is overwriiten with a command in infra/values.yaml
CMD ["java", \
    "-XX:InitialHeapSize=1024M", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar","/app.war"]