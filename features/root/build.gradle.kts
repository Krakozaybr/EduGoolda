plugins {
    alias(libs.plugins.convetion.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
}

android {
    namespace = "ru.itmo.edugoolda.features.root"
}

dependencies {
    ksp(libs.ktorfit.ksp)

    // Modules
    implementation(project(":core"))
    implementation(project(":features:auth"))
    implementation(project(":features:main"))
    implementation(project(":data:group"))
    implementation(project(":data:lesson"))
    implementation(project(":data:solutions"))
    implementation(project(":data:user"))
    implementation(project(":features:group"))
    implementation(project(":features:join_requests"))
    implementation(project(":features:lesson"))
    implementation(project(":data:auth"))
    implementation(project(":features:solution"))

    // Kotlin
    implementation(libs.kotlinx.datetime)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // UI
    implementation(libs.bundles.compose)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.coil)

    // DI
    implementation(libs.koin)

    // Logging
    implementation(libs.logger.kermit)

    // Network
    implementation(libs.bundles.ktor)
    implementation(libs.ktorfit.lib)

    implementation(libs.form.validation)

    // Architecture
    implementation(libs.bundles.decompose)
    implementation(libs.bundles.replica)
    api(libs.moko.resources)
    implementation(libs.moko.resourcesCompose)
}
