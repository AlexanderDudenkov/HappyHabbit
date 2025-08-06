// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    kotlin("kapt") version libs.versions.kotlin
    alias(libs.plugins.hilt) apply false
    id("jacoco")
}

tasks.withType<Test>().configureEach {
    finalizedBy("jacocoTestReport")
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class", "**/R$*.class",
        "**/BuildConfig.*", "**/Manifest*.*",
        "**/*Test*.*", "android/**/*.*"
    )

    val javaClasses = fileTree(layout.buildDirectory.dir("intermediates/javac/debug/classes")) {
        exclude(fileFilter)
    }

    val kotlinClasses = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(fileFilter)
    }

    classDirectories.setFrom(files(javaClasses, kotlinClasses))
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(fileTree(layout.buildDirectory.asFile).include("**/jacoco/testDebugUnitTest.exec"))
}