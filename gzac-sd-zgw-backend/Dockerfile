# FROM eclipse-temurin:17-alpine
FROM amazoncorretto:17.0.20-alpine@sha256:e1138bf0cca62e04692de650ffe8923f35c39fcb554458c7acd98efc2d135144

ADD /build/libs/gzac-sd-zgw-backend.war /app.war

# !! This cmd is overwriiten with a command in infra/values.yaml
CMD ["java", \
    "-XX:InitialHeapSize=1024M", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar","/app.war"]