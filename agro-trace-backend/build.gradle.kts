plugins {
    java
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.freefair.lombok") version "8.11"
}

group = "com.agrotrace"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Core
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // Database
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")
    implementation("org.liquibase:liquibase-core")
    implementation("org.hibernate.orm:hibernate-spatial:6.6.4.Final")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // OpenAPI / Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    // Cache
    implementation("com.github.ben-manes.caffeine:caffeine")

    // QR Generation
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")

    // AWS S3 (para almacenamiento de evidencias)
    implementation("software.amazon.awssdk:s3:2.29.20")

    // Resilience4j (Rate limiting, circuit breaker)
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.assertj:assertj-core:3.26.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Configurar annotation processors para que IDE los reconozca
tasks.withType<JavaCompile> {
    options.annotationProcessorGeneratedSourcesDirectory = file("build/generated/sources/annotationProcessor/java/main")
}
