group = "ca.cleaningdepot.tools"
version = "1.0.0"
description = "Gradle JiBX Plugin"

plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api(libs.org.jibx.jibx.tools)
    api(libs.org.jibx.jibx.bind)
    api(libs.org.jibx.jibx.extras)
    api(libs.org.jibx.jibx.run)
}

gradlePlugin {
    plugins {
        create("jibx-plugin") {
            id = "ca.cleaningdepot.tools.jibx-gradle-plugin"
            implementationClass = "ca.cleaningdepot.tools.jibx.JibxPlugin"
        }
    }
}
