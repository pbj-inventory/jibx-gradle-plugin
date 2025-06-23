package ca.cleaningdepot.tools.jibx

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import org.jibx.binding.Compile

open class JibxTask : DefaultTask() {
    @get:Input
    lateinit var configuration: JibxPluginExtension

    @get:InputFiles
    lateinit var classpath: FileCollection

    @TaskAction
    fun run() {
        val compiler = Compile()
        compiler.setLoad(false)
        compiler.setSkipValidate(false)
        compiler.setVerbose(false)
        compiler.setVerify(false)
        compiler.compile(classpath.toStringArray(), configuration.bindings.get().toStringArray())
    }

    private fun FileCollection.toStringArray() = this.files.map { it.absolutePath }.toTypedArray()
}