plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "org.example"
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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.4.1")
    implementation("org.modelmapper:modelmapper:3.2.2")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")
    //implementation("org.springframework.security:spring-security-web:6.4.2")
    //implementation("org.springframework.security:spring-security-core:6.4.2")
  //  implementation("org.springframework.security:spring-security-config:6.4.2")
    implementation("org.hibernate.common:hibernate-commons-annotations:6.0.6.Final")

    implementation("io.minio:minio:8.5.15")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
    implementation("org.postgresql:postgresql:42.7.4")

    //implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    //implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    //implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")




    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    //runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    //runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
