package ca.cleaningdepot.tools.jibx

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType

class JibxPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val configuration = project.extensions.create<JibxPluginExtension>("jibx")
        val jibxTask = project.tasks.register<JibxTask>("jibx") {
            this.configuration = configuration
            this.classpath = project.configurations["compileClasspath"].plus(
                project.the<JavaPluginExtension>().sourceSets.getByName("main").output
            )
        }
        project.tasks.withType<Jar>().configureEach { dependsOn(jibxTask) }
    }
}