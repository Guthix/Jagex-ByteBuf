@file:Suppress("UnstableApiUsage")

import io.guthix.buffer.registerPublication
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    idea
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
    kotlin("jvm")
}

group = "io.guthix"
version = "0.1.4"
description = "A Netty ByteBuf extension library for RuneTek obfuscated buffers"

val repoUrl = "https://github.com/guthix/Jagex-ByteBuf"
val gitSuffix = "github.com/guthix/Jagex-ByteBuf.git"

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

    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
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