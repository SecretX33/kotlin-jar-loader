import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm") version "2.0.0"
    application
}

val javaVersion = JvmTarget.JVM_21

dependencies {
    implementation(kotlin("reflect"))
}

tasks.jar { enabled = false }

artifacts.archives(tasks.shadowJar)

tasks.shadowJar {
    archiveFileName.set("${rootProject.name}.jar")
}

val mainClassName = "com.github.secretx33.jarloader.JarLoaderKt"

application {
    mainClass.set(mainClassName)
}

val buildExampleJars by tasks.registering {
    group = "build"
    dependsOn(":example-jar-one:jar", ":example-jar-two:jar", ":example-jar-three:jar")
}

tasks.build {
    dependsOn(buildExampleJars)
}

allprojects {
    apply(plugin = "kotlin")

    group = "com.github.secretx33"
    version = "0.1"

    repositories {
        mavenCentral()
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.withType<JavaCompile> {
        options.apply {
            release.set(javaVersion.target.toInt())
            options.encoding = "UTF-8"
        }
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
            jvmTarget.set(javaVersion)
        }
    }
}