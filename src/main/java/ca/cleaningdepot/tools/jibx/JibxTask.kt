package ca.cleaningdepot.tools.jibx

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.jibx.binding.Compile
import java.io.File
import java.net.URLClassLoader

@CacheableTask
abstract class JibxTask : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val bindings: Property<FileCollection>

    @get:InputFiles
    @get:CompileClasspath
    abstract val classpath: Property<FileCollection>

    @get:InputFiles
    @get:Classpath
    abstract val classRoots: Property<FileCollection>

    @get:OutputDirectory
    abstract val output: DirectoryProperty

    @TaskAction
    fun run() {
        val jibxDir = output.get().asFile
        classRoots.get().filter { it.exists() }.forEach { it.copyRecursively(jibxDir, true) }
        val compiler = Compile()
        compiler.setLoad(false)
        compiler.setSkipValidate(false)
        compiler.setVerbose(false)
        compiler.setVerify(false)
        val pluginClasspath =
            (this::class.java.classLoader as? URLClassLoader)?.urLs?.map { it.toString() } ?: emptyList()
        val classpath = classpath.get().files.plus(jibxDir).map { it.absolutePath }//
            .plus(pluginClasspath).toTypedArray()
        compiler.compile(classpath, bindings.get().files.toStringArray())
    }

    private fun Set<File>.toStringArray() = this.map { it.absolutePath }.toTypedArray()
}