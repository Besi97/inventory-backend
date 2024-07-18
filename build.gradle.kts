import java.net.URI

plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    kotlin("plugin.jpa") version "2.0.0"
    kotlin("kapt") version "2.0.0"
    `maven-publish`
}

group = "dev.besi.inventory"
version = "0.0.2"

fun gitlabRegistry(repositoryHandler: RepositoryHandler): MavenArtifactRepository = repositoryHandler.maven {
    url = URI(
        "https://${
            providers.gradleProperty("gitlab.domain").getOrElse(System.getenv("GITLAB_DOMAIN"))
        }/api/v4/projects/4/packages/maven"
    )
    credentials {
        username = providers.gradleProperty("gitlab.auth.username").getOrElse(System.getenv("GITLAB_USERNAME"))
        password = providers.gradleProperty("gitlab.auth.pat").getOrElse(System.getenv("GITLAB_PAT"))
    }
}

repositories {
    mavenCentral()
    gitlabRegistry(this)
}

val springBootVersion = "3.3.0"
val mapStructVersion = "1.5.5.Final"

dependencies {
    api("dev.besi.inventory.graphql:inventory-api-models-jvm:0.0.2")
    implementation("org.springframework.boot:spring-boot-starter-graphql:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.4.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.mapstruct:mapstruct:$mapStructVersion")
    kapt("org.mapstruct:mapstruct-processor:$mapStructVersion")
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

tasks.create("projectVersion") {
    doLast {
        println(version)
    }
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

publishing {
    publications {
        create<MavenPublication>("inventory-backend") {
            artifact(tasks.bootJar)
        }
    }
    repositories {
        gitlabRegistry(this)
    }
}
