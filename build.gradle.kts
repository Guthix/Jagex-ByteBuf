import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    idea
    `maven-publish`
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("com.github.hierynomus.license")
}

group = "io.guthix"
version = "0.1-SNAPSHOT"
description = "A Netty ByteBuf extension library for RuneTek obfuscated buffers."

val licenseHeader: File by extra(file("LGPLV3.txt"))

val licensePluginVersion: String by extra("0.15.0")
val logbackVersion: String by extra("1.2.3")
val nettyVersion: String by extra("4.1.50.Final")
val kotlinTestVersion: String by extra("3.4.2")
val kotlinVersion: String by extra(project.getKotlinPluginVersion()!!)

repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(group = "io.netty", name = "netty-buffer", version = nettyVersion)
    testImplementation(group = "io.kotlintest", name = "kotlintest-runner-junit5", version = kotlinTestVersion)
    testImplementation(group = "ch.qos.logback", name = "logback-classic", version = logbackVersion)
}

kotlin { explicitApi() }

license {
    header = licenseHeader
    exclude("**/*.xml")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }

    dokka {
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
    }
}

val dokkaJar: Jar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])
            artifact(dokkaJar)
            pom {
                url.set("https://github.com/guthix/Jagex-ByteBuf")
                licenses {
                    license {
                        name.set("GNU Lesser General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/lgpl-3.0.txt")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/guthix/Jagex-ByteBuf.git")
                    developerConnection.set("scm:git:ssh://github.com/guthix/Jagex-ByteBuf.git")
                }
            }
        }
    }
}