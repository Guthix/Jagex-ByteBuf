@file:Suppress("UnstableApiUsage")

plugins {
    idea
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.5.0"
    kotlin("jvm") version "1.5.30"
}

allprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    
    group = "io.guthix"

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(rootProject.libs.kotest.junit)
        testImplementation(rootProject.libs.kotest.assert)
        testImplementation(rootProject.libs.kotest.property)
        dokkaHtmlPlugin(rootProject.libs.dokka.java)
    }

    kotlin { explicitApi() }

    kotlin {
        jvmToolchain {
            (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of("8"))
        }
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()
        }
    }
}