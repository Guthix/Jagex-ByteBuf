@file:Suppress("UnstableApiUsage")

plugins {
    idea
    `maven-publish`
    signing
    kotlin("jvm") version "1.8.21"
}

allprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    
    group = "io.guthix"

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(rootProject.deps.kotest.junit)
        testImplementation(rootProject.deps.kotest.assert)
        testImplementation(rootProject.deps.kotest.property)
    }

    kotlin {
        explicitApi()
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(rootProject.deps.versions.jdk.get()))
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