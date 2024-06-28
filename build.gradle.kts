import java.net.URI

plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
}

group = "dev.besi.inventory"
version = "0.0.1-beta"

repositories {
    mavenCentral()
    maven {
        url = URI(
            "https://${
                providers.gradleProperty("gitlab.domain").getOrElse(System.getenv("GITLAB_DOMAIN"))
            }/api/v4/projects/3/packages/maven"
        )
        credentials {
            username = providers.gradleProperty("gitlab.auth.username").getOrElse(System.getenv("GITLAB_USERNAME"))
            password = providers.gradleProperty("gitlab.auth.pat").getOrElse(System.getenv("GITLAB_PAT"))
        }
    }
}

val springBootVersion = "3.3.0"

dependencies {
    api("dev.besi.inventory.graphql:inventory-api-models-jvm:0.0.1-beta")
    implementation("org.springframework.boot:spring-boot-starter-graphql:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.springframework.graphql:spring-graphql-test:1.3.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework:spring-webflux")
    testImplementation(kotlin("test"))
}

tasks.create<Copy>("copyGraphQLSchema") {
    dependsOn(configurations.compileClasspath)
    from(zipTree(configurations.compileClasspath.map { it.files.find { it.name.contains("inventory-api-models-jvm") } })) {
        include("*.graphqls")
        include("*.graphql")
        include("graphql.config.yml")
    }
    into(layout.projectDirectory.dir("src/main/resources/graphql"))
}

tasks.named("processResources") {
    dependsOn("copyGraphQLSchema")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
    jvmToolchain(21)
}
