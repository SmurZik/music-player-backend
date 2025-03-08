
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    kotlin("plugin.serialization") version "2.1.10"
}

group = "com.smurzik"
version = "0.0.1"

application {
    mainClass = "com.smurzik.ApplicationKt"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.cio)
    implementation(libs.logback.classic)
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.1")
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
