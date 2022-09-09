object BuildPlugins {
    val androidGradle = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
    val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltVersion}"
    val nav = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navVersion}"
}

object AndroidLibraries {
    val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val materialDesign = "com.google.android.material:material:${Versions.material}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val glideVersion = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
    val hilt = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
    val hiltKapt = "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"
    val hiltAndroidX = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltAndroidXVersion}"
    val hiltAndroidXKapt = "androidx.hilt:hilt-compiler:${Versions.hiltAndroidXVersion}"
    val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navVersion}"
    val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navVersion}"
}

object TestLibraries {
    val junit = "junit:junit:${Versions.jUnit}"

}

object ConfigData {
    const val applicationId = "com.byandev.storyapp"
    const val compileSdkVersion = 32
    const val buildToolsVersion = "30.0.3"
    const val minSdkVersion = 21
    const val targetSdkVersion = 32
    const val versionCodes = 1
    const val versionName = "1.0.0"


}