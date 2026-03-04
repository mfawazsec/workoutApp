buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    configurations.all {
        resolutionStrategy {
            force("io.gitlab.arturbosch.detekt:detekt-core:1.23.5")
        }
    }
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "17"
        reports {
            html.required.set(true)
        }
    }
}
