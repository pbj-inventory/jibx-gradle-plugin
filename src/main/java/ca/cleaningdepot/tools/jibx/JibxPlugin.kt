package ca.cleaningdepot.tools.jibx

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.file.ProjectLayout
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*

class JibxPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val configuration = project.extensions.create<JibxPluginExtension>("jibx")
        val jibxTask = project.tasks.register<JibxTask>("jibx") {
            this.bindings.set(configuration.bindings)
            this.classpath.set(project.configurations["compileClasspath"])
            this.classRoots.set(project.the<JavaPluginExtension>().sourceSets.named("main").map { it.output })
        }
        project.tasks.named<Jar>("jar") {
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
            from(project.layout.jibxDirectory)
            dependsOn(jibxTask)
        }
    }

    companion object {
        val ProjectLayout.jibxDirectory get() = this.buildDirectory.dir("jibx")
    }
}