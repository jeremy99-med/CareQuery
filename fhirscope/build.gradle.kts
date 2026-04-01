plugins {
    kotlin("jvm") version "1.9.23"
    // TODO: Add the kotlinx.serialization plugin if you choose kotlinx.serialization for JSON parsing
    // kotlin("plugin.serialization") version "1.9.23"
    application
}

group = "com.fhirscope"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // --- HTTP Client ---
    // OkHttp is used here for simplicity and wide community support.
    // TODO: If you prefer Ktor Client (coroutine-native), replace these with:
    //   implementation("io.ktor:ktor-client-core:2.3.x")
    //   implementation("io.ktor:ktor-client-cio:2.3.x")   // CIO engine (JVM)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // --- JSON Parsing ---
    // Jackson is used here; it pairs well with OkHttp and requires no compiler plugin.
    // TODO: If you prefer kotlinx.serialization, replace with:
    //   implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.x")
    //   and add @Serializable annotations to your model classes.
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")

    // --- Kotlin stdlib ---
    implementation(kotlin("stdlib"))

    // --- Testing (optional, add when ready) ---
    // TODO (Future): Add JUnit 5 + MockK for unit tests
    // testImplementation("org.junit.jupiter:junit-jupiter:5.10.x")
    // testImplementation("io.mockk:mockk:1.13.x")
}

application {
    // Entry point — matches the main function in Main.kt
    mainClass.set("com.fhirscope.MainKt")
}

kotlin {
    jvmToolchain(17)
}
