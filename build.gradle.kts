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

val logbackVersion: String by extra("1.2.3")
val nettyVersion: String by extra("4.1.60.Final")
val kotestVersion: String by extra("4.4.3")
val kotlinVersion: String by extra(project.getKotlinPluginVersion()!!)

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    api(group = "io.netty", name = "netty-buffer", version = nettyVersion)
    testImplementation(group = "ch.qos.logback", name = "logback-classic", version = logbackVersion)
    testImplementation(group = "io.kotest", name = "kotest-runner-junit5-jvm", version = kotestVersion)
    testImplementation(group = "io.kotest", name = "kotest-assertions-core-jvm", version = kotestVersion)
    dokkaHtmlPlugin(group = "org.jetbrains.dokka", name = "kotlin-as-java-plugin", version = kotlinVersion)
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