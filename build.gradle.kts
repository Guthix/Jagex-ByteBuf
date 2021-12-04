@file:Suppress("UnstableApiUsage")

plugins {
    idea
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.5.0"
    kotlin("jvm") version "1.6.0"
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
        testImplementation(rootProject.deps.kotest.junit)
        testImplementation(rootProject.deps.kotest.assert)
        testImplementation(rootProject.deps.kotest.property)
        dokkaHtmlPlugin(rootProject.deps.dokka.java)
    }

    kotlin {
        explicitApi()
        jvmToolchain {
            (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(rootProject.deps.versions.jdk.get()))
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