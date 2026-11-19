    kotlin("jvm")
}

val gdxVersion: String by rootProject.extra
val kotlinVersion: String by rootProject.extra
val enableGraalNative: String? by project
val graalHelperVersion: String? by project

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {
    api("com.badlogicgames.gdx:gdx:$gdxVersion")
           api("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
    api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    implementation("io.github.libktx:ktx-graphics:1.12.1-b1")
    implementation("io.github.libktx:ktx-app:1.12.1-b1")
    implementation("io.github.libktx:ktx-math:1.12.1-b1")

    if (enableGraalNative == "true") {
        implementation("io.github.berstanio:gdx-svmhelper-annotations:$graalHelperVersion")
    }
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
