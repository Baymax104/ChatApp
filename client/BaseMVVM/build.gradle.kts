plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val kotlinVersion by extra("1.9.0")

@Suppress("UnstableApiUsage")
android {
    namespace = "com.baymax104.basemvvm"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles(File("consumer-rules.pro"))
    }

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
    viewBinding { enable = true }
    dataBinding { enable = true }


}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.core:core-ktx:$kotlinVersion")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0")

    implementation("androidx.activity:activity-ktx:1.6.0")
    implementation("androidx.fragment:fragment-ktx:1.5.1")

    api("io.github.jeremyliao:live-event-bus-x:1.8.0")

    api("com.kunminx.arch:unpeek-livedata:7.8.0")

    api("com.blankj:utilcodex:1.31.1")

    api("com.jaeger.statusbarutil:library:1.5.1")
}
