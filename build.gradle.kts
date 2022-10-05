version = "1.0.0"

plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
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

application {
    mainClass.set("me.cjcrafter.webb.ui.Main")
}

//tasks.run.get().workingDir = File("/run")