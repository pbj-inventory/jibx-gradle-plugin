package ca.cleaningdepot.tools.jibx

import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

abstract class JibxPluginExtension {
    @get:Input
    abstract val bindings: Property<FileCollection>
}
