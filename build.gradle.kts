import org.gradle.api.tasks.compile.JavaCompile

buildscript {
    val kotlinVersion: String by extra("1.9.20")

    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
        google()
        maven { url = uri("https://central.sonatype.com/repository/maven-snapshots/") }
    }
    
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2") // Проверь версию, 8.9.3 может быть еще не в стабиле
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    apply(plugin = "eclipse")
    apply(plugin = "idea")

    // Настройка IDEA для корректного вывода классов
    extensions.configure<org.gradle.plugins.ide.idea.model.IdeaModel> {
        module {
            outputDir = file("build/classes/java/main")
            testOutputDir = file("build/classes/java/test")
        }
    }
}

val projectVersion: String by extra("1.0.0") // Убедись, что версия определена

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

// Конфигурация для всех подпроектов, кроме android
configure(subprojects.filter { it.name != "android" }) {
    apply(plugin = "java-library")
    
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
            assetsFile.delete()
            val files = fileTree(assetsFolder).map { assetsFolder.relativeTo(it).path }.sorted()
            assetsFile.writeText(files.joinToString("\n") + "\n")
        }
    }

    tasks.named("processResources") {
        dependsOn("generateAssetList")
    }

    tasks.withType<JavaCompile> {
        options.isIncremental = true
    }
}

eclipse {
    project {
        name = "My game-parent"
    }
}
