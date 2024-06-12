import kotlin.io.path.div
import kotlin.io.path.moveTo

tasks.jar {
    outputs.upToDateWhen { false }
    archiveFileName.set("${project.name}.jar")
    doLast {
        val currentJarFile = archiveFile.get().asFile.toPath()
        val libsFolder = rootProject.layout.buildDirectory.get().asFile.toPath() / "libs"
        val newJarFile = libsFolder / currentJarFile.fileName
        currentJarFile.moveTo(newJarFile, overwrite = true)
    }
}