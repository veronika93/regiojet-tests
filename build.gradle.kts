import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.31"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation(group = "org.assertj", name = "assertj-core", version = "3.8.0")
    testImplementation(group = "io.rest-assured", name = "rest-assured", version = "4.3.1")
    testImplementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.12.0-rc1")
    testImplementation(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310", version = "2.12.0-rc1")
    testImplementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = "2.12.0-rc1")

    testImplementation ("com.codeborne:selenide:5.15.0")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

// config JVM target to 1.8 for kotlin compilation tasks
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}
