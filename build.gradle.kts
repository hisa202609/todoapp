plugins {
    java
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("org.example.todoapp")                      // ★ プロジェクト名
    mainClass.set("org.example.todoapp.HelloApplication")      // ★ プロジェクト 名 + メインのクラス名
}

// JavaFX（Windows 用）。授業の PC はすべて Windows のため "win" を指定名名名名名名名ｍｓｒメインメイン
val javafxVersion = "25.0.1"
dependencies {
    for (m in listOf("base", "graphics", "controls", "fxml")) {
        implementation("org.openjfx:javafx-$m:$javafxVersion:win")
    }
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-native-access=javafx.graphics")
}