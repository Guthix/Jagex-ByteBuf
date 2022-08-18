@file:Suppress("UnstableApiUsage")

enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "jagex-bytebuf"

dependencyResolutionManagement {
    versionCatalogs {
        create("deps") {
            version("jdk", "8")
            version("kotlin", "1.7.0")
            version("dokka", "1.7.0")
            version("serialization", "1.2.2")
            version("netty4", "4.1.70.Final")
            version("logback", "1.3.1")
            version("kotest", "5.3.1")
            library("dokka-java", "org.jetbrains.dokka", "kotlin-as-java-plugin").versionRef("dokka")
            library("serialization-core", "org.jetbrains.kotlinx", "kotlinx-serialization-core")
                .versionRef("serialization")
            library("netty4-buffer", "io.netty", "netty-buffer").versionRef("netty4")
            library("netty4-codec", "io.netty", "netty-codec").versionRef("netty4")
            library("logback-classic", "ch.qos.logback", "logback-classic").versionRef("logback")
            library("kotest-junit", "io.kotest", "kotest-runner-junit5-jvm").versionRef("kotest")
            library("kotest-assert", "io.kotest", "kotest-assertions-core-jvm").versionRef("kotest")
            library("kotest-property", "io.kotest", "kotest-property").versionRef("kotest")
        }
    }
}

include("netty4")
include("netty4:serialization")
include("netty4:extensions")
include("netty4:wrapper")