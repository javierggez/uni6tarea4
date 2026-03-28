package com.example.uni6tarea4

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initFirebase()
    }

    private fun initFirebase() {
        try {
            FirebaseApp.initializeApp(this)
            FirebaseCrashlytics.getInstance().apply {
                setCrashlyticsCollectionEnabled(true)
                setCustomKey("app_version", "1.0")
                log("App iniciada correctamente")
            }
            Log.d("App", "Firebase Crashlytics inicializado")
        } catch (e: Exception) {
            Log.e("App", "Error al inicializar Firebase: ${e.message}")
        }
    }
}
