package com.github.secretx33.jarloader

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.extension
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.toPath

fun main() {
    val libsFolder = getLibsFolder()
    val jarsToLoad = libsFolder.takeIf { it.isDirectory() }?.listDirectoryEntries().orEmpty()
        .filter { it.isRegularFile() && it.extension == "jar" && it.name.startsWith("example-") }
        .ifEmpty {
            System.err.println("No example jars found in folder '$libsFolder'. Did you forget to build the example jars? If so, please check the project README file.")
            return
        }

    val jarClassLoader = JarClassLoader(parent = Dummy::class.java.classLoader)
    jarsToLoad.forEach {
        jarClassLoader.addURL(it.toUri().toURL())
    }

    val mainClasses = listOf(
        "ExampleJarOneKt",
        "ExampleJarTwoKt",
        "ExampleJarThreeKt"
    ).map(::mainClass).also {
        println("Main classes to load: $it")
    }

    println("Executing main methods from loaded jars...")
    mainClasses.forEach {
        val mainClass = jarClassLoader.loadClass(it)
        val mainMethod = mainClass.getDeclaredMethod("main").apply { isAccessible = true }
        mainMethod.invoke(null)
    }

    // Close the jar class loader to release the resources
    jarClassLoader.close()
}

private fun mainClass(name: String): String = "com.github.secretx33.$name"

private fun getLibsFolder(): Path = getProjectFolder() / "build" / "libs"

private fun getProjectFolder(): Path = Path(Dummy::class.java.protectionDomain.codeSource.location.toURI().toPath().toAbsolutePath().invariantSeparatorsPathString
    .substringBeforeLast("/build/"))

private class Dummy