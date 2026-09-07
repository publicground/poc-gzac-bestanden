# FROM eclipse-temurin:17-alpine
FROM amazoncorretto:17-alpine@sha256:8aa46a55845b61ba079f8289556fcc1a7887cdf303d360bc27140ab38300d44e

ADD /build/libs/gzac-sd-zgw-backend.war /app.war

# !! This cmd is overwriiten with a command in infra/values.yaml
CMD ["java", \
    "-XX:InitialHeapSize=1024M", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar","/app.war"]