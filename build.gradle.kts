import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import java.net.URI

plugins {
    idea
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
    kotlin("jvm")
}

group = "io.guthix"
version = "0.1.1"
description = "A Netty ByteBuf extension library for RuneTek obfuscated buffers"

val repoUrl = "https://github.com/guthix/Jagex-ByteBuf"
val gitSuffix = "github.com/guthix/Jagex-ByteBuf.git"

val logbackVersion: String by extra("1.2.3")
val nettyVersion: String by extra("4.1.56.Final")
val kotestVersion: String by extra("4.3.2")
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

publishing {
    repositories {
        maven {
            name = "MavenCentral"
            url = URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
    }
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
            pom {
                name.set("Jagex ByteBuf")
                description.set(rootProject.description)
                url.set(repoUrl)
                licenses {
                    license {
                        name.set("APACHE LICENSE, VERSION 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                scm {
                    connection.set("scm:git:git://$gitSuffix")
                    developerConnection.set("scm:git:ssh://$gitSuffix")
                    url.set(repoUrl
                    )
                }
                developers {
                    developer {
                        id.set("bart")
                        name.set("Bart van Helvert")
                    }
                }
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(System.getenv("SIGNING_KEY"), System.getenv("SIGNING_PASSWORD"))
    sign(publishing.publications["default"])
}