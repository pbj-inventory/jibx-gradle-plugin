package ca.cleaningdepot.tools.jibx

import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles

abstract class JibxPluginExtension {
    @get:InputFiles
    abstract val bindings: Property<FileCollection>
}
