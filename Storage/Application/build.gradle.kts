plugins {
    id("java")
    id("application")
    id("io.spring.dependency-management")
}

group = "org.marssiii"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("Application.Main")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.1.0")
    }
}

dependencies {
    implementation(project(":Storage:Business"))
    implementation(project(":Storage:Infrastructure"))
    implementation(project(":Storage:Presentation"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")

    implementation("com.fasterxml.jackson.core:jackson-databind")

    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Ваш драйвер базы данных
    runtimeOnly("org.postgresql:postgresql")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.test {
    useJUnitPlatform()
}