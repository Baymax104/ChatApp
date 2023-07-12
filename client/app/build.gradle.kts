plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.baymax104.chatapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.baymax104.chatapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    @Suppress("UnstableApiUsage")
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    dataBinding { enable = true }
    viewBinding { enable = true }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(project(":BaseMVVM"))

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0")
    kapt("com.android.databinding:compiler:3.1.4")

    implementation("androidx.activity:activity-ktx:1.6.0")
    implementation("androidx.fragment:fragment-ktx:1.5.1")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.li-xiaojun:XPopup:2.9.19")
    implementation("com.github.liangjingkanji:StateLayout:1.4.1")

    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.14.2")

    implementation("com.tencent:mmkv:1.2.15")

    implementation("io.github.scwang90:refresh-layout-kernel:2.0.6")
    implementation("io.github.scwang90:refresh-header-classics:2.0.6")

    implementation("top.zibin:Luban:1.1.8")

}