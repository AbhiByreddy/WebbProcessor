import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

version = "1.0.0"

buildscript {
    repositories {
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.openjfx:javafx-plugin:0.0.13")
    }
}
apply(plugin = "org.openjfx.javafxplugin")

plugins {
    java
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

configurations {
    compileClasspath.get().extendsFrom(create("shadeOnly"))
}

repositories {
    mavenCentral()
}

dependencies {
}


javafx {
    version = "19"
    modules("javafx.controls", "javafx.fxml")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(17) // We need to set release compatibility to java 17 since MC 18+ uses it
    }
}

tasks.named<ShadowJar>("shadowJar") {

    destinationDirectory.set(file("../build"))
    archiveFileName.set("WebbProcessor-${version}.jar")
    configurations = listOf(project.configurations["shadeOnly"], project.configurations["runtimeClasspath"])
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}