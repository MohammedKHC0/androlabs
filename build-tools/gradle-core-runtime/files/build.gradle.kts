plugins {
    id("androlabs.jvm.library")
}

dependencies {
    implementation(project(":build-tools:base-annotations"))
    implementation(project(":build-tools:functional"))

    implementation(libs.guava)
    implementation(libs.slf4j)
}