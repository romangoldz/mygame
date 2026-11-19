// 1. Подключаем плагины IDE в самом начале, чтобы блок eclipse { } заработал
plugins {
    id("eclipse")
    id("idea")
}

buildscript {
    // Версию лучше задать как обычную переменную
    val kotlinVersion = "1.9.20" 

    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
        google()
        maven { url = uri("https://central.sonatype.com/repository/maven-snapshots/") }
    }
    
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

// 2. Исправляем синтаксис extra (теперь через set)
val projectVersion = "1.0.0"
extra.set("projectVersion", projectVersion)
extra.set("gdxVersion", "1.12.1") // Укажи свою версию gdx

allprojects {
    apply(plugin = "eclipse")
    apply(plugin = "idea")

    // Настройка IDEA
    extensions.configure<org.gradle.plugins.ide.idea.model.IdeaModel> {
        module {
            outputDir = file("build/classes/java/main")
            testOutputDir = file("build/classes/java/test")
        }
    }
}

subprojects {
    version = projectVersion
    extra.set("appName", "My game")
    
    repositories {
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://central.sonatype.com/repository/maven-snapshots/") }
        maven { url = uri("https://jitpack.io") }
    }
}

// 3. Конфигурация всех проектов кроме android
configure(subprojects.filter { it.name != "android" }) {
    apply(plugin = "java-library")
    
    // Правильный способ задать совместимость Java
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.register("generateAssetList") {
        val assetsFolder = file("${project.rootDir}/assets/")
        val assetsFile = file("${assetsFolder}/assets.txt")

        inputs.dir(assetsFolder)
        outputs.file(assetsFile)

        doLast {
            if (assetsFolder.exists()) {
                assetsFile.delete()
                val files = fileTree(assetsFolder).map { assetsFolder.relativeTo(it).path }.sorted()
                assetsFile.writeText(files.joinToString("\n") + "\n")
            }
        }
    }

    tasks.named("processResources") {
        dependsOn("generateAssetList")
    }

    tasks.withType<JavaCompile> {
        options.isIncremental = true
    }
}

// 4. Теперь блок eclipse работает корректно
eclipse {
    project {
        name = "My game-parent"
    }
}