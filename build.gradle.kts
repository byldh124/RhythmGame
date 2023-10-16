buildscript {
    dependencies {
        classpath(libs.kotlin.gradleplugin)
        classpath(libs.google.service)
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins{
    alias(libs.plugins.android.application) apply false
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}