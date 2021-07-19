@file:Suppress("UnstableApiUsage")

import io.guthix.buffer.registerPublication

plugins {
    idea
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.4.32"
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.serialization") version "1.5.0"
}

group = "io.guthix"
version = "0.2.0-snapshot"
description = "A Netty ByteBuf extension library for RuneTek obfuscated buffers"

allprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }

    dependencies {
        testImplementation(rootProject.libs.logback)
        testImplementation(rootProject.libs.kotest.junit)
        testImplementation(rootProject.libs.kotest.assert)
        testImplementation(rootProject.libs.kotest.property)
        dokkaHtmlPlugin(rootProject.libs.dokka)
    }

    kotlin { explicitApi() }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()
        }
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                useOldBackend = true
            }
        }
    }
}

dependencies {
    api(libs.netty.buf)
}

registerPublication(
    publicationName = "jagexByteBuf",
    pomName = "jagex-bytebuf"
)