rootProject.name = "UpchainApp"

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("org.hnau.plugin.settings") version "1.8.2"
}

hnau {
    groupId = "org.hnau.upchainapp"
}
