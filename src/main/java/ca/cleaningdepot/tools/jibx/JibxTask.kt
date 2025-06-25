package ca.cleaningdepot.tools.jibx

import ca.cleaningdepot.tools.jibx.JibxPlugin.Companion.jibxDirectory
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import org.jibx.binding.Compile
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

@CacheableTask
abstract class JibxTask @Inject constructor(private val layout: ProjectLayout) : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val bindings: Property<FileCollection>

    @get:InputFiles
    @get:CompileClasspath
    abstract val classpath: Property<FileCollection>

    @get:InputFiles
    @get:Classpath
    abstract val classRoots: Property<FileCollection>

    @get:OutputFile
    val output: Provider<RegularFile> = layout.jibxDirectory.map { it.file("jibx-output") }

    @TaskAction
    fun run() {
        val jibxDir = layout.jibxDirectory.get().asFile
        classRoots.get().filter { it.exists() }.forEach { it.copyRecursively(jibxDir, true) }
        val compiler = Compile()
        compiler.setLoad(false)
        compiler.setSkipValidate(false)
        compiler.setVerbose(false)
        compiler.setVerify(false)
        compiler.compile(classpath.get().files.plus(jibxDir).toStringArray(), bindings.get().files.toStringArray())
        val hash = hash(
            classpath.get().map { it.absolutePath.toByteArray() }
                .plus(layout.jibxDirectory.get().asFileTree.map { it.readBytes() })
                .plus(bindings.get().map { it.readBytes() })
        )
        output.get().asFile.writeText(hash)
    }

    private fun hash(items: Iterable<ByteArray>): String {
        val sha256 = MessageDigest.getInstance("SHA-256")
        items.forEach { sha256.update(it) }
        return BigInteger(1, sha256.digest()).toString(16).padStart(sha256.digestLength * 2, '0')
    }

    private fun Set<File>.toStringArray() = this.map { it.absolutePath }.toTypedArray()
}