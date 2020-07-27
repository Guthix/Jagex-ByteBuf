import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    idea
    `maven-publish`
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

group = "io.guthix"
version = "0.1"
description = "A Netty ByteBuf extension library for RuneTek obfuscated buffers"

val logbackVersion: String by extra("1.2.3")
val nettyVersion: String by extra("4.1.51.Final")
val kotestVersion: String by extra("4.1.3")
val kotlinVersion: String by extra(project.getKotlinPluginVersion()!!)

repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(group = "io.netty", name = "netty-buffer", version = nettyVersion)
    testImplementation(group = "ch.qos.logback", name = "logback-classic", version = logbackVersion)
    testImplementation(group = "io.kotest", name = "kotest-runner-junit5-jvm", version = kotestVersion)
    testImplementation(group = "io.kotest", name = "kotest-assertions-core-jvm", version = kotestVersion)
}

// kotlin { explicitApi() }

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
                        name.set("APACHE LICENSE, VERSION 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
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