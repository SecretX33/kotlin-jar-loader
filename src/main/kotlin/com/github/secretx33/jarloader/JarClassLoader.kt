package com.github.secretx33.jarloader

import java.net.URL
import java.net.URLClassLoader
import java.net.URLStreamHandlerFactory
import kotlin.io.path.Path

class JarClassLoader : URLClassLoader {

    constructor(urls: Array<URL> = emptyArray()) : super(urls)

    constructor(urls: Array<URL> = emptyArray(), parent: ClassLoader) : super(urls, parent)

    constructor(urls: Array<URL> = emptyArray(), parent: ClassLoader, factory: URLStreamHandlerFactory) : super(urls, parent, factory)

    constructor(name: String, urls: Array<URL> = emptyArray(), parent: ClassLoader) : super(name, urls, parent)

    constructor(name: String, urls: Array<URL> = emptyArray(), parent: ClassLoader, factory: URLStreamHandlerFactory) : super(name, urls, parent, factory)

    public override fun addURL(url: URL?) {
        super.addURL(url)
    }

    /**
     * Required for Java Agents when this classloader is used as the system classloader
     */
    @Suppress("unused", "SpellCheckingInspection")
    private fun appendToClassPathForInstrumentation(jarfile: String) {
        addURL(Path(jarfile).toRealPath().toUri().toURL())
    }

    override fun toString(): String = "${this::class.simpleName}(name=$name, urls=${urLs.map { it.toString() }}, parent=$parent)"

    companion object {
        init {
            registerAsParallelCapable()
        }
    }
}