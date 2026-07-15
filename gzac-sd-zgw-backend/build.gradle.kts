import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun
import java.io.ByteArrayOutputStream

plugins {
    war
    // Idea
    idea
    id("org.jetbrains.gradle.plugin.idea-ext")

    // Spring
    id("org.springframework.boot")
    id("io.spring.dependency-management")

    // Kotlin
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")

    // Docker-compose plugin
    id("com.avast.gradle.docker-compose")

    // Spring boot actuator generator
    id("com.gorylenko.gradle-git-properties")

    // Checkstyle
    id("com.diffplug.spotless")

    // SonarQube
    id("org.sonarqube")

    id("io.mateo.cxf-codegen")

}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

java {
    toolchain {
        version = JavaVersion.VERSION_17
    }
}

idea {
    module {
        isDownloadSources = true
    }
}

tasks {
    war.configure {
        archiveFileName = "${project.name}-plain.war"
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    bootWar.configure {
        archiveFileName = "${project.name}.war"
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    bootJar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://s01.oss.sonatype.org/content/groups/staging/") }
    maven { url = uri("https://repo.ritense.com/repository/maven-public/") }
    maven { url = uri("https://repo.ritense.com/repository/maven-snapshot/") }
}

val jacksonVersion: String by project
val kotlinCoroutinesVersion: String by project
val mainClassPath: String by project
val valtimoVersion: String by project

dependencyManagement {
    imports {
        mavenBom("com.fasterxml.jackson:jackson-bom:$jacksonVersion")
    }
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.hibernate.orm:hibernate-core:6.5.2.Final")

    // Valtimo
    implementation(platform("com.ritense.valtimo:valtimo-dependency-versions:$valtimoVersion"))
    implementation("com.ritense.valtimo:valtimo-gzac-dependencies")
    implementation("com.ritense.valtimo:wordpress-mail:$valtimoVersion")
    implementation("com.ritense.valtimo:besluit:$valtimoVersion")
    implementation("com.ritense.valtimo:objects-api:$valtimoVersion")
    implementation("com.ritense.valtimo:openzaak-resource:$valtimoVersion")
    implementation("com.ritense.valtimo:openzaak:$valtimoVersion")
    implementation("com.ritense.valtimo:smartdocuments:$valtimoVersion")
    implementation("com.ritense.valtimoplugins:freemarker:8.1.1-V12")
    implementation("com.ritense.valtimoplugins:object-management:0.4.0")
    implementation("com.ritense.valtimoplugins:externe-klanttaak:1.0.2")
    implementation("com.ritense.valtimoplugins:document-search:1.0.0")
    implementation("com.ritense.valtimoplugins:suwinet:2.1.2")
    implementation("com.ritense.valtimoplugins:suwinet-auth:1.0.1")
    implementation("com.ritense.valtimoplugins:socrates:1.4.0")
    implementation("com.ritense.valtimoplugins:http-client-authentication-plugin:1.0.0")
    implementation("com.ritense.valtimoplugins:valuemapper:1.2.0")

    // conditional runtime-only dependencies
    if (Os.isFamily(Os.FAMILY_MAC)) {
        if (Os.isArch("aarch64")) {
            println("   - Applying Mac OS (aarch64) specific dependencies.")

            runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.75.Final:osx-aarch_64")
        }
    }

    // Netty and WebClient
    implementation("io.projectreactor.netty:reactor-netty-core:1.1.20")
    implementation("io.projectreactor.netty:reactor-netty-http:1.1.20")
    implementation("org.springframework:spring-webflux:6.1.14")

    // Postgresql
    implementation("org.postgresql:postgresql:42.7.11")

    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${kotlinCoroutinesVersion}")
    implementation("io.github.microutils:kotlin-logging")

    // Jackson
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:${jacksonVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${jacksonVersion}")

    // Metrics
    implementation("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Testing
    testImplementation("com.ritense.valtimo:test-utils-common")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.camunda.bpm.assert:camunda-bpm-assert:15.0.0")
    testImplementation("org.camunda.bpm.extension:camunda-bpm-junit5:1.1.0")
    testImplementation("org.camunda.bpm.extension:camunda-bpm-assert:1.2")
    testImplementation("org.camunda.bpm.extension:camunda-bpm-assert-scenario:1.1.1")
    testImplementation("org.camunda.bpm.extension.mockito:camunda-bpm-mockito:5.16.0")
    testImplementation("org.mockito:mockito-core:5.4.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("com.h2database:h2")
    testImplementation("mysql:mysql-connector-java:8.0.33")

    // CXF Codegen
    cxfCodegen("jakarta.xml.ws:jakarta.xml.ws-api:4.0.2")
    cxfCodegen("jakarta.annotation:jakarta.annotation-api:3.0.0")
    cxfCodegen("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    cxfCodegen("jakarta.jws:jakarta.jws-api:3.0.0")

    // Apache CXF and Jakarta dependencies
    implementation("org.apache.cxf:cxf-rt-frontend-jaxws:4.0.7")
    implementation("org.apache.cxf:cxf-rt-transports-http:4.0.7")
    implementation("com.sun.xml.ws:jaxws-ri:4.0.3")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.5")

    implementation("org.apache.cxf:cxf-tools-common:4.0.7")
    implementation("org.apache.cxf:cxf-tools-wsdlto-core:4.0.7")
    implementation("org.apache.cxf:cxf-tools-wsdlto-databinding-jaxb:4.0.7")
    implementation("org.apache.cxf:cxf-tools-wsdlto-frontend-jaxws:4.0.7")
}

buildscript {
    apply(from = "gradle/environment.gradle.kts")
}

apply(from = "gradle/testing.gradle.kts")

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.register<BootRun>(name = "bootRunWithCompose") {
    group = "application"
    description = "Starts docker containers and then runs this project as a Spring Boot application"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass = mainClassPath
    jvmArgs("-Duser.timezone=UTC")

    dependsOn(tasks.composeUp)
}

tasks.register<Exec>("removeProjectContainers") {
    group = "application"
    description = "Removes all containers for the current project. Use this to start from scratch."

    println("Attempting to remove all containers for project: ${project.name}")
    commandLine("docker", "ps", "-a", "--filter", "name=${project.name}", "-q")
    standardOutput = ByteArrayOutputStream()

    doLast {
        val containers = standardOutput.toString().trimIndent().split("\n")

        exec {
            val baseCommand = mutableListOf("docker", "rm")
            baseCommand.addAll(containers)

            commandLine(baseCommand)
        }
    }
}

dockerCompose {
    setProjectName("$name-${valtimoVersion.substringBefore(".")}") // uses projectRoot.name as the container group name

    stopContainers = true // doesn't call `docker-compose down` if set to false; default is true
    removeContainers = false // containers are retained upon composeDown for persistent storage
    removeVolumes = false // set to false while we use local keycloak

    createNested("testConfiguration").apply {
        isRequiredBy(tasks.named<Test>("integrationTest"))
        setProjectName("${name}-test")
        useComposeFiles = listOf("docker-compose-integration-test.yml")
        removeContainers = true
        removeVolumes = true
    }
}

// This is a workaround for https://github.com/avast/gradle-docker-compose-plugin/issues/199
@Suppress("DEPRECATION")
gradle.taskGraph.afterTask {
    if (this.name == "bootRunWithCompose" && (state.failure != null || state.didWork)) {
        logger.log(LogLevel.WARN, "Stopping docker containers gracefully.")
        tasks.composeDown.get().down()
    }
}

