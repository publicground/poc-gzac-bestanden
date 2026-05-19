FROM eclipse-temurin:17-alpine

ADD /build/libs/gzac-sd-zgw-backend.war /app.war
ADD /DigiCertGlobalG2TLSRSASHA2562020CA1-1.crt.pem /DigiCertGlobalG2TLSRSASHA2562020CA1-1.crt.pem

RUN keytool -import -alias DigiCertGlobalG2 -keystore /opt/java/openjdk/lib/security/cacerts -storepass changeit -file /DigiCertGlobalG2TLSRSASHA2562020CA1-1.crt.pem

# !! This cmd is overwriiten with a command in infra/values.yaml
CMD ["java", \
    "-XX:InitialHeapSize=1024M", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar","/app.war"]