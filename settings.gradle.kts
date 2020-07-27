pluginManagement {
    val kotlinVersion by extra("1.3.71")
    val dokkaVersion by extra("0.10.0")

    plugins {
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.dokka") version dokkaVersion
    }

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

rootProject.name = "jagex-bytebuf"
