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
            version("netty", "4.1.70.Final")
            version("logback", "1.3.1")
            version("kotest", "5.3.1")
            alias("dokka-java").to("org.jetbrains.dokka", "kotlin-as-java-plugin").versionRef("dokka")
            alias("serialization-core").to("org.jetbrains.kotlinx", "kotlinx-serialization-core")
                .versionRef("serialization")
            alias("netty-buffer").to("io.netty", "netty-buffer").versionRef("netty")
            alias("netty-codec").to("io.netty", "netty-codec").versionRef("netty")
            alias("logback-classic").to("ch.qos.logback", "logback-classic").versionRef("logback")
            alias("kotest-junit").to("io.kotest", "kotest-runner-junit5-jvm").versionRef("kotest")
            alias("kotest-assert").to("io.kotest", "kotest-assertions-core-jvm").versionRef("kotest")
            alias("kotest-property").to("io.kotest", "kotest-property").versionRef("kotest")
        }
    }
}

include("serialization")
include("extensions")
include("wrapper")