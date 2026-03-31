plugins {
    id(
        hnau.plugins.kotlin.serialization
            .get()
            .pluginId,
    )
    id(
        hnau.plugins.hnau.jvm
            .get()
            .pluginId,
    )
    application
}

dependencies {
    implementation(libs.kotlinx.cli)
    implementation(libs.upchain.core)
    implementation(libs.upchain.sync.client.http)
}

application {
    mainClass = "org.hnau.upchainapp.client.MainKt"
}

tasks.installDist {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
