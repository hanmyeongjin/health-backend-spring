import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("org.asciidoctor.convert") version "1.5.8"
    jacoco
    kotlin("kapt") version "1.7.10"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
}

group = "kr.co.fitpet"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

var snippetsDir by extra { file("build/generated-snippets") }

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.3")

    implementation("org.springdoc:springdoc-openapi-ui:1.6.9")

    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")

    runtimeOnly("mysql:mysql-connector-java")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    /*Querydsl*/
    implementation("com.querydsl:querydsl-jpa")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    kapt(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa")

    /* redis */
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.4.1")
    testImplementation("io.kotest:kotest-assertions-core:5.4.1")
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    testImplementation("io.mockk:mockk:1.12.5")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    outputs.dir(snippetsDir)
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.asciidoctor {
    inputs.dir(snippetsDir)
    dependsOn(tasks.test)
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }

    dependsOn(tasks.test) // tests are required to run before generating the report
}

jacoco {
    toolVersion = "0.8.8"
}

tasks.jacocoTestCoverageVerification {
    var qDomains = mutableListOf<String>()

    for (qPattern in 'A' .. 'Z') {
        qDomains.add("*.Q${qPattern}*")
    }

    val excludesClass = mutableListOf<String>()
    excludesClass.addAll(qDomains)

    violationRules {
        rule {
            element = "CLASS"

            // 커버리지 체크를 제외할 클래스들
            excludes = excludesClass
        }
    }
}