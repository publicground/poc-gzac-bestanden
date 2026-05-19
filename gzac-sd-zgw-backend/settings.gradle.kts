// Background info https://github.com/gradle/gradle/issues/1697
pluginManagement {
    val kotlinVersion: String by settings
    val kotlinCoroutinesVersion: String by settings
    val springVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val dockerComposeVersion: String by settings
    val springActuatorPropertiesGeneratorVersion: String by settings
    val ktlintVersion: String by settings
    val spotlessVersion: String by settings
    val ideaExt: String by settings
    val sonarqubeVersion: String by settings
    val cxfCodegenVersion: String by settings

    plugins {
        // Idea
        idea
        id("org.jetbrains.gradle.plugin.idea-ext") version ideaExt

        // Spring
        id("org.springframework.boot") version springVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion

        // Kotlin
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion
        kotlin("plugin.allopen") version kotlinVersion

        // Docker-compose plugin
        id("com.avast.gradle.docker-compose") version dockerComposeVersion

        // Spring boot actuator generator
        id("com.gorylenko.gradle-git-properties") version springActuatorPropertiesGeneratorVersion

        // Checkstyle
        id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
        id("com.diffplug.spotless") version spotlessVersion

        id("org.sonarqube") version sonarqubeVersion

        id("io.mateo.cxf-codegen") version cxfCodegenVersion
    }
}

val projectName: String by settings
rootProject.name = projectName