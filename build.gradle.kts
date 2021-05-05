@file:Suppress("UnstableApiUsage")

import io.guthix.buffer.registerPublication

plugins {
    idea
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
    kotlin("jvm")
}

group = "io.guthix"
version = "0.2.0-snapshot"
description = "A Netty ByteBuf extension library for RuneTek obfuscated buffers"

repositories {
    mavenCentral()
}

dependencies {
    api(libs.netty.buf)
    testImplementation(libs.logback)
    testImplementation(libs.kotest.junit)
    testImplementation(libs.kotest.assert)
    dokkaHtmlPlugin(libs.dokka)
}

kotlin { explicitApi() }

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

registerPublication(
    publicationName = "jagexByteBuf",
    pomName = "jagex-bytebuf"
)