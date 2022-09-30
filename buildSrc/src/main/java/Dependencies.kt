object Versions {
    const val gradlePlugin = "7.1.3"
    const val kotlin = "1.6.21"
    const val timber = "4.7.1"
    const val appCompat = "1.5.1"
    const val legacySupport = "1.0.0"
    const val coreKtx = "1.8.0"
    const val material = "1.6.1"
    const val constraintLayout = "2.1.4"
    const val jUnit = "4.12"
    const val junitAnroidTest = "1.1.3"
    const val espressoCoreAnroidTest = "3.4.0"
    const val glideVersion = "4.12.0"
    const val glideKaptVersion = "4.12.0"
    const val hiltVersion = "2.38.1"
    const val hiltAndroidXVersion = "1.0.0-alpha03"
    const val navVersion = "2.5.1"
    const val retrofitVersion = "2.9.0"
    const val okHttpVersion = "5.0.0-alpha.2"
    const val moshiVersion = "2.9.0"
    const val moshiKotlinVersion = "1.14.0"
    const val gsonVersion = "2.9.0"
    const val rxJava3Version = "3.0.2"
    const val rxAndroidVersion = "3.0.0"
    const val lifeCylcleVersion = "2.3.1"
    const val coroutinesVersion = "1.5.1"
    const val pagingVersion = "3.0.1"
    const val fragmentKtxVersion = "1.3.6"
    const val splashScreenVersion = "1.0.0-beta02"
    const val cardViews = "1.0.0"
    const val recyclerViews = "1.2.1"
    const val circleImageViews = "3.1.0"
    const val lottie = "3.4.1"
    const val easyPermission = "1.0.0"
    const val cameraVersion = "1.1.0-alpha08"
    const val cameraViewVersion = "1.0.0-alpha28"
    const val photoView = "2.3.0"
    const val compressImg = "3.0.1"
    const val playServiceMapVersion = "18.1.0"
    const val playServiceLocationVersion = "20.0.0"
    const val mapsPlatform = "2.0.0"
    const val swipeRefreshVersion = "1.1.0"

    const val mockitoVersion = "4.8.0"
    const val androidXTestVersion = "androidXTestVersion"
}

object BuildPlugins {
    const val androidGradle = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltVersion}"
    const val nav = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navVersion}"
    const val mapsPlatform = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:${Versions.mapsPlatform}"
}

object AndroidLibraries {
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val legacySupportV4 = "androidx.legacy:legacy-support-v4:${Versions.legacySupport}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val materialDesign = "com.google.android.material:material:${Versions.material}"
    const val cardView = "androidx.cardview:cardview:${Versions.cardViews}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerViews}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val glideVersion = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
    const val kaptGlide = "com.github.bumptech.glide:compiler:${Versions.glideKaptVersion}"
    const val hilt = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
    const val hiltKapt = "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"
    const val hiltAndroidX = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltAndroidXVersion}"
    const val hiltAndroidXKapt = "androidx.hilt:hilt-compiler:${Versions.hiltAndroidXVersion}"
    const val navigation = "androidx.navigation:navigation-fragment-ktx:${Versions.navVersion}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navVersion}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val okHttp = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpVersion}"
    const val retrofitRxJava3 = "com.squareup.retrofit2:adapter-rxjava3:${Versions.moshiVersion}"
    const val moshiCoverter = "com.squareup.retrofit2:converter-moshi:${Versions.moshiVersion}"
    const val moshiKaptKotlin = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshiKotlinVersion}"
    const val moshiKotlin = "com.squareup.moshi:moshi:${Versions.moshiKotlinVersion}"
    const val gson = "com.google.code.gson:gson:${Versions.gsonVersion}"
    const val rxJava = "io.reactivex.rxjava3:rxjava:${Versions.rxJava3Version}"
    const val rxAndroid = "io.reactivex.rxjava3:rxandroid:${Versions.rxAndroidVersion}"
    const val lifecycleService = "androidx.lifecycle:lifecycle-service:${Versions.lifeCylcleVersion}"
    const val lifecycleViewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifeCylcleVersion}"
    const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifeCylcleVersion}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVersion}"
    const val pagingKtx = "androidx.paging:paging-runtime-ktx:${Versions.pagingVersion}"
    const val pagingRxJava = "androidx.paging:paging-rxjava3:${Versions.pagingVersion}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtxVersion}"
    const val splashScreen = "androidx.core:core-splashscreen:${Versions.splashScreenVersion}"
    const val circleImageView = "de.hdodenhof:circleimageview:${Versions.circleImageViews}"
    const val lottieAnimation = "com.airbnb.android:lottie:${Versions.lottie}"
    const val easyPermission = "com.vmadalin:easypermissions-ktx:${Versions.easyPermission}"
    const val cameraCore = "androidx.camera:camera-core:${Versions.cameraVersion}"
    const val camera2 = "androidx.camera:camera-camera2:${Versions.cameraVersion}"
    const val cameraLifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraVersion}"
    const val cameraView = "androidx.camera:camera-view:${Versions.cameraViewVersion}"
    const val cameraExtension = "androidx.camera:camera-extensions:${Versions.cameraViewVersion}"
    const val photoView = "com.github.chrisbanes:PhotoView:${Versions.photoView}"
    const val compressImg = "id.zelory:compressor:${Versions.compressImg}"
    const val playServiceMaps = "com.google.android.gms:play-services-maps:${Versions.playServiceMapVersion}"
    const val playServiceLocation = "com.google.android.gms:play-services-location:${Versions.playServiceLocationVersion}"
    const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshVersion}"
}

object TestLibraries {
    const val junit = "junit:junit:${Versions.jUnit}"
    const val mockitoTest = "org.mockito:mockito-core:${Versions.mockitoVersion}"
}

object AndroidTestLibraries {
    const val androidXTest = "androidx.test:core:${Versions.androidXTestVersion}"
    const val junitAnroidTest = "androidx.test.ext:junit:${Versions.junitAnroidTest}"
    const val espressoCoreAnroidTest = "androidx.test.espresso:espresso-core:${Versions.espressoCoreAnroidTest}"

}

object ConfigData {
    const val applicationId = "com.byandev.storyapp"
    const val compileSdkVersion = 32
    const val buildToolsVersion = "30.0.3"
    const val minSdkVersion = 23
    const val targetSdkVersion = 32
    const val versionCodes = 1
    const val versionName = "1.0.0"


}