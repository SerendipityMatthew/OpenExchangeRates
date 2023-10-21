import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.com.android.test) apply false
}
true // Needed to make the Suppress annotation work for the plugins block

buildscript {
    dependencies {
        // other plugins...
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.detekt.gradle.plugin)
    }
}
val sarifReportMerge by tasks.registering(ReportMergeTask::class) {
    output.set(rootProject.buildDir.resolve("reports/detekt/merged_report.sarif"))
}

subprojects {

    /**
     * Start Configuring Detekt
     */
    coreDetektSetup()

    beforeEvaluate {
        dependencies {
            detektPlugins(libs.detekt.formatting)
            detektPlugins(libs.twitter.compose.detekt)
        }
    }
}

/**
 * To run detekt simply:
 * 1. ./gradlew module:detekt for each module
 * 2. ./gradlew detekt for whole project
 */
fun Project.coreDetektSetup() {
    // Apply Plugin to sub-project
    apply(plugin = "io.gitlab.arturbosch.detekt")

    // Configure Detekt
    detekt {
        config = files("$rootDir/config/detekt/default-detekt-config.yml")
        buildUponDefaultConfig = true
        ignoredBuildTypes = listOf("release")
        source = files(
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_JAVA,
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_TEST_SRC_DIR_JAVA,
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_TEST_SRC_DIR_KOTLIN,
        )
    }

    tasks.withType<Detekt>().configureEach detekt@{
        exclude("**/build/**", "**/generated/**", "**/resources/**")
        basePath = rootProject.projectDir.absolutePath
        autoCorrect = true // Auto corrects common formatting issues
        // Configure reports here
        reports {
            xml.required.set(false)
            txt.required.set(false)
            md.required.set(false)

            html {
                required.set(true)
                outputLocation.set(
                    layout.buildDirectory.file("reports/detekt.html")
                )
            }

            sarif.required.set(true)
        }

        // Merged Report
        finalizedBy(sarifReportMerge)
        sarifReportMerge.configure {
            input.from(this@detekt.sarifReportFile)
        }
    }

    tasks.withType<DetektCreateBaselineTask>().configureEach detekt@{
        exclude("**/build/**", "**/generated/**", "**/resources/**")
        basePath = rootProject.projectDir.absolutePath
    }
}
