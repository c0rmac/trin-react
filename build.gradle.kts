
val kotlinSerializationVersion: String by rootProject

plugins {
    kotlin("multiplatform") version "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
    // kotlin("jvm") version "1.5.31"
}

group = "me.cormaccinnsealach"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

kotlin {
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            distribution {
                directory = File("$projectDir/server/app/src/main/resources/static")
            }
        }
    }

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerializationVersion")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.206-kotlin-1.5.10")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.206-kotlin-1.5.10")

                //Kotlin Styled (chapter 3)
                implementation("org.jetbrains:kotlin-styled:1.0.0-pre.112-kotlin-1.4.0")

                implementation(npm("@material-ui/core", "4.9.14"))
                implementation(npm("@material-ui/icons", "4.9.1"))

                implementation(npm("react-spring", "8.0.27"))
                // implementation(npm("@jetbrains/kotlin-css", "1.0.0-pre.89"))
                // implementation(npm("@jetbrains/kotlin-css-js", "1.0.0-pre.89"))
                implementation("org.jetbrains:kotlin-react-router-dom:5.1.2-pre.112-kotlin-1.4.0")

                implementation(npm("react-router-transition", "2.0.0"))

                implementation(npm("glamor", "2.20.40"))

                implementation(npm("react-visibility-sensor", "5.1.1"))
                implementation(npm("react-animate-height", "2.0.21"))

                // Gallery
                implementation(npm("react-photo-gallery", "8.0.0"))

                // Full Page Scrolling
                implementation(npm("react-full-page", "0.1.9"))
                implementation(npm("core-js", "2.5.7"))

                // Geocoding
                implementation(npm("react-geocode", "0.2.1"))

                implementation(npm("react-helmet", "6.1.0"))

                implementation(npm("@aws-sdk/client-s3", "3.30.0"))
                implementation(npm("@aws-sdk/client-cognito-identity", "3.30.0"))
                implementation(npm("@aws-sdk/credential-provider-cognito-identity", "3.30.0"))

                implementation(npm("@stripe/react-stripe-js", "1.5.0"))
                implementation(npm("@stripe/stripe-js", "1.18.0"))
            }
        }
        val jvmMain by getting {
            dependencies {
                // implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.+")
                // implementation("com.fasterxml.jackson.core:jackson-core:2.11.+")
            }
        }
    }
}