plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("com.google.devtools.ksp")
    //safe args
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.example.todoapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.todoapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }

}


dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // Use for Bottom Navigation Bar
    implementation ("com.gauravk.bubblenavigation:bubblenavigation:1.0.7")

    // Room Database
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.5.2")
    // To use Kotlin annotation processing tool (kapt)
    ksp("androidx.room:room-compiler:2.5.2")

    // For flow, coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")


    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation ("androidx.activity:activity-ktx:1.7.2")
    implementation ("androidx.fragment:fragment-ktx:1.6.1")

    // Gson
    implementation ("com.google.code.gson:gson:2.10.1")


    // Navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.3.5")

    implementation ("com.afollestad.material-dialogs:color:3.2.1")

    // Horizontal Calendar
    implementation ("devs.mulham.horizontalcalendar:horizontalcalendar:1.3.4")

    // RecyclerView
    implementation ("it.xabaras.android:recyclerview-swipedecorator:1.4")
    // Work Manager
    implementation("androidx.work:work-runtime-ktx:2.8.0")

}